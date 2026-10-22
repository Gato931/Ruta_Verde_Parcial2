
package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.Actividad;
import co.edu.uniquindio.poo.model.Agencia;
import co.edu.uniquindio.poo.model.ConsecutivoReserva;
import co.edu.uniquindio.poo.model.EstadoReserva;
import co.edu.uniquindio.poo.model.Reserva;
import co.edu.uniquindio.poo.model.ResultadoConfirmacion;
import co.edu.uniquindio.poo.model.ResultadoPago;
import co.edu.uniquindio.poo.model.excepcion.DominioException;
import co.edu.uniquindio.poo.model.repositorio.RegistroReservas;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// FACADE: las pantallas solo llaman a confirmar; no conocen el proceso interno.
public class FachadaConfirmacionReserva {
    private final Agencia agencia;
    private final RegistroReservas registroReservas;
    private final ConsecutivoReserva consecutivoReserva;
    private final ValidadorTarjeta validadorTarjeta;
    private final PasarelaPago pasarelaPago;
    private final ServicioCorreo servicioCorreo;

    public FachadaConfirmacionReserva(Agencia agencia, RegistroReservas registroReservas,
            ConsecutivoReserva consecutivoReserva, ValidadorTarjeta validadorTarjeta,
            PasarelaPago pasarelaPago, ServicioCorreo servicioCorreo) {
        this.agencia = agencia;
        this.registroReservas = registroReservas;
        this.consecutivoReserva = consecutivoReserva;
        this.validadorTarjeta = validadorTarjeta;
        this.pasarelaPago = pasarelaPago;
        this.servicioCorreo = servicioCorreo;
    }

    // Synchronized evita confirmar simultaneamente dos reservas por esta misma
    // fachada.
    public synchronized ResultadoConfirmacion confirmar(Reserva reserva, String numeroTarjeta,
            GeneradorVoucher generadorVoucher) {
        if (reserva == null)
            return new ResultadoConfirmacion(false, "La reserva es obligatoria", "");
        boolean cobroAprobado = false;
        String autorizacion = "";
        List<Actividad> descontadas = new ArrayList<>();
        try {
            if (generadorVoucher == null)
                throw new DominioException("Debe seleccionar el formato del voucher");
            if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
                throw new DominioException("La reserva ya fue procesada");
            }
            if (reserva.getFechaViaje().isBefore(LocalDate.now())) {
                throw new DominioException("La fecha del viaje ya paso");
            }

            // 1. Verificar disponibilidad y cupos antes de llamar a la pasarela.
            List<Actividad> actividades = reserva.getProducto().obtenerActividades();
            if (actividades.isEmpty())
                throw new DominioException("El producto no contiene actividades");
            Set<String> codigos = new HashSet<>();
            for (Actividad actividad : actividades) {
                if (!codigos.add(actividad.getCodigo().toLowerCase())) {
                    throw new DominioException("El paquete tiene actividades repetidas");
                }
                if (!actividad.estaDisponible()) {
                    throw new DominioException("Actividad suspendida: " + actividad.getNombre());
                }
                if (!actividad.hayCupo(reserva.getFechaViaje(), reserva.getNumeroViajeros())) {
                    throw new DominioException("No hay cupo suficiente en " + actividad.getNombre());
                }
            }

            // 2. Validar la tarjeta.
            if (!validadorTarjeta.esValida(numeroTarjeta)) {
                throw new DominioException("El numero de tarjeta no supera la validacion de Luhn");
            }

            // 3. Cobrar exactamente el importe que se va a registrar.
            long total = reserva.calcularValorTotal();
            String referencia = "RV-" + reserva.getCliente().getDocumento() + "-" + reserva.getFechaViaje();
            ResultadoPago pago = pasarelaPago.cobrar(numeroTarjeta, total, referencia);
            if (!pago.isAprobado())
                throw new DominioException("Pago rechazado: " + pago.getMotivo());
            cobroAprobado = true;
            autorizacion = pago.getAutorizacion();

            // 4. Descontar los cupos.
            for (Actividad actividad : actividades) {
                actividad.reservarCupo(reserva.getFechaViaje(), reserva.getNumeroViajeros());
                descontadas.add(actividad);
            }

            // 5. Guardar una reserva confirmada con importe historico fijo.
            reserva.confirmar(consecutivoReserva.siguiente(), autorizacion);
            registroReservas.registrar(reserva);

            // 6. Generar el voucher.
            String voucher;
            try {
                voucher = generadorVoucher.emitir(reserva, agencia);
            } catch (RuntimeException exception) {
                String mensaje = "Reserva " + reserva.getNumero()
                        + " confirmada y cobrada, pero no se pudo emitir el voucher. Autorizacion: " + autorizacion;
                enviarSinInterrumpir(reserva, "Aviso sobre reserva confirmada", mensaje);
                return new ResultadoConfirmacion(true, mensaje, "");
            }

            // 7. El correo no debe deshacer un cobro ya aprobado.
            try {
                servicioCorreo.enviar(reserva.getCliente().getCorreo(),
                        "Reserva confirmada " + reserva.getNumero(), voucher);
                return new ResultadoConfirmacion(true,
                        "Reserva confirmada correctamente: " + reserva.getNumero(), voucher);
            } catch (RuntimeException exception) {
                return new ResultadoConfirmacion(true,
                        "Reserva confirmada: " + reserva.getNumero()
                                + ". El correo no se pudo enviar; conserve el voucher.",
                        voucher);
            }
        } catch (RuntimeException exception) {
            // Si la pasarela ya cobro, su API NO permite reversar el dinero.
            for (Actividad actividad : descontadas) {
                actividad.liberarCupo(reserva.getFechaViaje(), reserva.getNumeroViajeros());
            }
            String motivo = exception.getMessage() == null ? "Error al confirmar" : exception.getMessage();
            if (cobroAprobado) {
                String aviso = "Pago aprobado (" + autorizacion
                        + "), pero la confirmacion tuvo un error. Contacte la agencia; no vuelva a pagar. " + motivo;
                enviarSinInterrumpir(reserva, "Pago pendiente de revision", aviso);
                return new ResultadoConfirmacion(false, aviso, "");
            }
            reserva.marcarFallida();
            enviarSinInterrumpir(reserva, "Reserva no confirmada", motivo);
            return new ResultadoConfirmacion(false, motivo, "");
        }
    }

    private void enviarSinInterrumpir(Reserva reserva, String asunto, String contenido) {
        try {
            servicioCorreo.enviar(reserva.getCliente().getCorreo(), asunto, contenido);
        } catch (RuntimeException exception) {
            System.err.println("No fue posible enviar el correo: " + exception.getMessage());
        }
    }
}
