package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.Voucher;
import co.edu.uniquindio.poo.model.VoucherKiosco;

public class GeneradorVoucherKiosco extends GeneradorVoucher {
    @Override
    protected Voucher crearVoucher() {
        return new VoucherKiosco();
    }
}
