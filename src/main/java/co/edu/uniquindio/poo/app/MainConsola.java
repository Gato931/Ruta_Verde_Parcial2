package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.*;
import co.edu.uniquindio.poo.model.excepcion.DominioException;
import co.edu.uniquindio.poo.model.servicio.GeneradorVoucherAsesor;

import java.time.LocalDate;
import java.util.List;

public class MainConsola {
    public static void main(String[] args) {
        ContextoApp contexto = new ContextoApp();
        Cliente cliente = new Cliente("12345678", "Ana Perez", "3001234567",
                "ana@correo.com", "Colombia", "Espanol");
        Guia guia = new Guia("G1234", "Luis Gomez", List.of("Espanol", "Ingles"),
                "3101234567", 120_000);
        Actividad cafe = new Actividad("A01", "Finca cafetera", "Salento", 5,
                100_000, 10, Dificultad.BAJA, EstadoActividad.DISPONIBLE);
        Actividad aves = new Actividad("A02", "Avistamiento de aves", "Filandia", 6,
                80_000, 10, Dificultad.MEDIA, EstadoActividad.DISPONIBLE);
        Paquete paquete = new Paquete("P01", "Quindio total", 10);
        paquete.agregar(cafe);
        paquete.agregar(aves);

        contexto.getRegistroClientes().registrar(cliente);
        contexto.getRegistroGuias().registrar(guia);
        contexto.getRegistroActividades().registrar(cafe);
        contexto.getRegistroActividades().registrar(aves);
        contexto.getRegistroPaquetes().registrar(paquete);

        LocalDate fecha = LocalDate.now().plusDays(5);
        Reserva reserva = new ReservaBuilder(cliente, paquete, fecha, 2)
                .conGuia(guia).conTransporteHotel(true).conSeguroViaje(true).construir();
        ResultadoConfirmacion resultado = contexto.getFachadaConfirmacionReserva()
                .confirmar(reserva, "4539 1488 0343 6467", new GeneradorVoucherAsesor());
        comprobar(resultado.isExitosa(), "La reserva debe confirmarse");
        comprobar(cafe.cuposDisponibles(fecha) == 8, "El cupo debe disminuir en dos");
        comprobar(contexto.getServicioReportes().generar(fecha, fecha).getIngresos() == reserva.calcularValorTotal(),
                "El reporte debe incluir la reserva");

        rechazar(() -> new Cliente("12#45", "Ana Perez", "3001234567", "ana@correo.com",
                "Colombia", "Espanol"), "Documento con simbolos");
        rechazar(() -> new Cliente("123456", "Ana45", "3001234567", "ana@correo.com",
                "Colombia", "Espanol"), "Nombre con numeros");
        rechazar(() -> new Cliente("123456", "Ana Perez", "telefono", "ana@correo.com",
                "Colombia", "Espanol"), "Telefono con letras");
        rechazar(() -> new Cliente("123456", "Ana Perez", "3001234567", "sin-arroba",
                "Colombia", "Espanol"), "Correo mal escrito");
        rechazar(() -> new Guia("GUIA2", "Luis4", List.of("Ingles"), "3001234567", 50000),
                "Guia con nombre incorrecto");
        rechazar(() -> new Actividad("A1", "Ruta de cafe", "Salento", 0, 5000,
                10, Dificultad.BAJA, EstadoActividad.DISPONIBLE), "Duracion cero");
        rechazar(() -> new Paquete("P03", "Ruta cafetera", 25), "Descuento mayor de 20");
        rechazar(() -> new ReservaBuilder(cliente, cafe, fecha, 16).construir(),
                "Mas de 15 viajeros");
        comprobar(!new co.edu.uniquindio.poo.model.servicio.ValidadorTarjeta()
                .esValida("4539 1488 0343 6468"), "Tarjeta Luhn invalida");

        System.out.println("PRUEBAS CORRECTAS: confirmacion y validaciones de datos");
    }

    private static void rechazar(Runnable accion, String descripcion) {
        try {
            accion.run();
        } catch (DominioException e) {
            System.out.println("Rechazo correcto [" + descripcion + "]: " + e.getMessage());
            return;
        }
        throw new AssertionError("Debia rechazar: " + descripcion);
    }

    private static void comprobar(boolean condicion, String descripcion) {
        if (!condicion)
            throw new AssertionError(descripcion);
    }
}
