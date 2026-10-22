package co.edu.uniquindio.poo.model;

public class ResultadoConfirmacion {
    private final boolean exitosa;
    private final String mensaje;
    private final String voucher;

    public ResultadoConfirmacion(boolean exitosa, String mensaje, String voucher) {
        this.exitosa = exitosa;
        this.mensaje = mensaje;
        this.voucher = voucher;
    }

    public boolean isExitosa() {
        return exitosa;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getVoucher() {
        return voucher;
    }
}
