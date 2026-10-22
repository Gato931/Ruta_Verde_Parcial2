
package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.Agencia;
import co.edu.uniquindio.poo.model.Reserva;
import co.edu.uniquindio.poo.model.Voucher;
import co.edu.uniquindio.poo.model.VoucherKiosco;

// FACTORY METHOD: cada subclase decide que tipo de voucher crear.
public abstract class GeneradorVoucher {
    protected abstract Voucher crearVoucher();

    public final String emitir(Reserva reserva, Agencia agencia) {
        Voucher voucher = crearVoucher();
        String encabezado = agencia.getNombreComercial() + " | RNT: " + agencia.getRnt();
        String pie = "Politica de cancelacion: sin costo hasta 48 horas antes del viaje.";
        String separador = voucher instanceof VoucherKiosco ? " | " : "\n";
        return encabezado + separador + voucher.generarDetalle(reserva) + separador + pie;
    }
}
