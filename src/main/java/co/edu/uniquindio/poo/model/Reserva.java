
package co.edu.uniquindio.poo.model;

import co.edu.uniquindio.poo.model.excepcion.DominioException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reserva {
    public static final long VALOR_TRANSPORTE_POR_VIAJERO = 35_000;
    public static final long VALOR_SEGURO_POR_VIAJERO = 15_000;

    private String numero;
    private final Cliente cliente;
    private final ProductoTuristico producto;
    private final LocalDate fechaViaje;
    private final int numeroViajeros;
    private final Guia guia;
    private final boolean transporteHotel;
    private final boolean seguroViaje;
    private final String observaciones;
    private EstadoReserva estado = EstadoReserva.PENDIENTE;
    private String autorizacionPago;
    private Long valorConfirmado;
    private List<Actividad> actividadesConfirmadas;

    public Reserva(Cliente cliente, ProductoTuristico producto, LocalDate fechaViaje, int numeroViajeros,
            Guia guia, boolean transporteHotel, boolean seguroViaje, String observaciones) {
        if (cliente == null)
            throw new DominioException("El cliente es obligatorio");
        if (producto == null)
            throw new DominioException("El producto es obligatorio");
        if (fechaViaje == null || fechaViaje.isBefore(LocalDate.now())) {
            throw new DominioException("La fecha del viaje no puede estar en el pasado");
        }
        if (numeroViajeros < 1 || numeroViajeros > 15) {
            throw new DominioException("El grupo debe tener entre 1 y 15 viajeros");
        }
        if (producto.tieneActividadSuspendida()) {
            throw new DominioException("El producto contiene una actividad suspendida");
        }
        if (producto.obtenerActividades().isEmpty()) {
            throw new DominioException("El producto debe contener alguna actividad");
        }
        if (guia != null && !guia.hablaIdioma(cliente.getIdiomaPreferido())) {
            throw new DominioException("El guia no habla el idioma preferido del cliente");
        }
        if (observaciones != null && observaciones.length() > 500) {
            throw new DominioException("Las observaciones no pueden superar 500 caracteres");
        }
        this.cliente = cliente;
        this.producto = producto;
        this.fechaViaje = fechaViaje;
        this.numeroViajeros = numeroViajeros;
        this.guia = guia;
        this.transporteHotel = transporteHotel;
        this.seguroViaje = seguroViaje;
        this.observaciones = observaciones == null ? "" : observaciones;
    }

    public long calcularValorTotal() {
        if (valorConfirmado != null)
            return valorConfirmado;
        long total = Math.multiplyExact(producto.calcularPrecioPorPersona(), numeroViajeros);
        if (transporteHotel)
            total = Math.addExact(total,
                    Math.multiplyExact(VALOR_TRANSPORTE_POR_VIAJERO, numeroViajeros));
        if (seguroViaje)
            total = Math.addExact(total,
                    Math.multiplyExact(VALOR_SEGURO_POR_VIAJERO, numeroViajeros));
        if (guia != null)
            total = Math.addExact(total,
                    Math.multiplyExact(guia.getTarifaPorDia(), calcularDiasGuia()));
        return total;
    }

    public int calcularDiasGuia() {
        return (producto.calcularDuracionHoras() + 7) / 8;
    }

    public void confirmar(String numero, String autorizacionPago) {
        if (estado != EstadoReserva.PENDIENTE) {
            throw new DominioException("La reserva ya fue procesada");
        }
        if (numero == null || numero.isBlank() || autorizacionPago == null || autorizacionPago.isBlank()) {
            throw new DominioException("Faltan los datos de confirmacion de la reserva");
        }
        long total = calcularValorTotal();
        List<Actividad> actividades = new ArrayList<>(producto.obtenerActividades());
        if (producto instanceof Paquete paquete)
            paquete.bloquearCambios();
        this.valorConfirmado = total;
        this.actividadesConfirmadas = Collections.unmodifiableList(actividades);
        this.numero = numero;
        this.autorizacionPago = autorizacionPago;
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public void marcarFallida() {
        if (estado == EstadoReserva.PENDIENTE)
            estado = EstadoReserva.FALLIDA;
    }

    public List<Actividad> getActividadesReservadas() {
        return actividadesConfirmadas == null ? producto.obtenerActividades() : actividadesConfirmadas;
    }

    public String getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public ProductoTuristico getProducto() {
        return producto;
    }

    public LocalDate getFechaViaje() {
        return fechaViaje;
    }

    public int getNumeroViajeros() {
        return numeroViajeros;
    }

    public Guia getGuia() {
        return guia;
    }

    public boolean isTransporteHotel() {
        return transporteHotel;
    }

    public boolean isSeguroViaje() {
        return seguroViaje;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public String getAutorizacionPago() {
        return autorizacionPago;
    }

    public long getValorTotal() {
        return calcularValorTotal();
    }

    public String getClienteNombre() {
        return cliente.getNombreCompleto();
    }

    public String getProductoNombre() {
        return producto.getNombre();
    }

    @Override
    public String toString() {
        return (numero == null ? "PENDIENTE" : numero) + " - " + cliente.getNombreCompleto()
                + " - " + producto.getNombre();
    }
}
