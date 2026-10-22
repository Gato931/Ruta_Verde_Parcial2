package co.edu.uniquindio.poo.model;

public class ResultadoPago {
    private final boolean aprobado;
    private final String motivo;
    private final String autorizacion;

    private ResultadoPago(boolean aprobado, String motivo, String autorizacion) {
        this.aprobado = aprobado;
        this.motivo = motivo;
        this.autorizacion = autorizacion;
    }

    public static ResultadoPago aprobado(String autorizacion) {
        return new ResultadoPago(true, "Pago aprobado", autorizacion);
    }

    public static ResultadoPago rechazado(String motivo) {
        return new ResultadoPago(false, motivo, "");
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getAutorizacion() {
        return autorizacion;
    }
}
