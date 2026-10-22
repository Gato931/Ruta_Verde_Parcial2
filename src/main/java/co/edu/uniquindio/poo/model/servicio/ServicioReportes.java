package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.EstadoReserva;
import co.edu.uniquindio.poo.model.Reserva;
import co.edu.uniquindio.poo.model.ResultadoReporte;
import co.edu.uniquindio.poo.model.excepcion.DominioException;
import co.edu.uniquindio.poo.model.repositorio.RegistroReservas;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ServicioReportes {
    private final RegistroReservas registroReservas;

    public ServicioReportes(RegistroReservas registroReservas) {
        this.registroReservas = registroReservas;
    }

    public ResultadoReporte generar(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null || fin.isBefore(inicio)) {
            throw new DominioException("El periodo del reporte no es valido");
        }

        long ingresos = 0;
        Map<String, Integer> viajerosPorProducto = new HashMap<>();
        Map<String, String> nombres = new HashMap<>();

        for (Reserva reserva : registroReservas.listar()) {
            if (reserva.getEstado() != EstadoReserva.CONFIRMADA)
                continue;
            LocalDate fecha = reserva.getFechaViaje();
            if (fecha.isBefore(inicio) || fecha.isAfter(fin))
                continue;

            ingresos += reserva.calcularValorTotal();
            String codigo = reserva.getProducto().getCodigo();
            viajerosPorProducto.put(codigo,
                    viajerosPorProducto.getOrDefault(codigo, 0) + reserva.getNumeroViajeros());
            nombres.put(codigo, reserva.getProducto().getNombre());
        }

        String productoMayor = "Sin reservas en el periodo";
        int mayor = 0;
        for (Map.Entry<String, Integer> entrada : viajerosPorProducto.entrySet()) {
            if (entrada.getValue() > mayor) {
                mayor = entrada.getValue();
                productoMayor = nombres.get(entrada.getKey());
            }
        }
        return new ResultadoReporte(ingresos, productoMayor, mayor);
    }
}
