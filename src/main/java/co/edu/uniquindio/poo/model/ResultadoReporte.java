package co.edu.uniquindio.poo.model;

public class ResultadoReporte {
    private final long ingresos;
    private final String productoMasViajeros;
    private final int cantidadViajeros;

    public ResultadoReporte(long ingresos, String productoMasViajeros, int cantidadViajeros) {
        this.ingresos = ingresos;
        this.productoMasViajeros = productoMasViajeros;
        this.cantidadViajeros = cantidadViajeros;
    }

    public long getIngresos() {
        return ingresos;
    }

    public String getProductoMasViajeros() {
        return productoMasViajeros;
    }

    public int getCantidadViajeros() {
        return cantidadViajeros;
    }
}
