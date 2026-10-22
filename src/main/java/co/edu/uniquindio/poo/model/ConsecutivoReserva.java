package co.edu.uniquindio.poo.model;

public class ConsecutivoReserva {
    private int numero = 0;

    public String siguiente() {
        numero++;
        return String.format("RV-%04d", numero);
    }
}
