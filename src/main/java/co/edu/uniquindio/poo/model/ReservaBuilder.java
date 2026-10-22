package co.edu.uniquindio.poo.model;

import java.time.LocalDate;

// Patron creacional BUILDER: separa obligatorios de los servicios opcionales de una reserva.
public class ReservaBuilder {
    private final Cliente cliente;
    private final ProductoTuristico producto;
    private final LocalDate fechaViaje;
    private final int numeroViajeros;
    private Guia guia;
    private boolean transporteHotel;
    private boolean seguroViaje;
    private String observaciones = "";

    public ReservaBuilder(Cliente cliente, ProductoTuristico producto, LocalDate fechaViaje, int numeroViajeros) {
        this.cliente = cliente;
        this.producto = producto;
        this.fechaViaje = fechaViaje;
        this.numeroViajeros = numeroViajeros;
    }

    public ReservaBuilder conGuia(Guia guia) {
        this.guia = guia;
        return this;
    }

    public ReservaBuilder conTransporteHotel(boolean transporteHotel) {
        this.transporteHotel = transporteHotel;
        return this;
    }

    public ReservaBuilder conSeguroViaje(boolean seguroViaje) {
        this.seguroViaje = seguroViaje;
        return this;
    }

    public ReservaBuilder conObservaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public Reserva construir() {
        return new Reserva(cliente, producto, fechaViaje, numeroViajeros, guia,
                transporteHotel, seguroViaje, observaciones);
    }
}
