package co.edu.uniquindio.poo.model;

public class VoucherKiosco implements Voucher {
    @Override
    public String generarDetalle(Reserva reserva) {
        return "TIQUETE " + reserva.getNumero() + " | " + reserva.getFechaViaje()
                + " | " + reserva.getNumeroViajeros() + " viajeros | CODIGO: " + reserva.getNumero();
    }
}
