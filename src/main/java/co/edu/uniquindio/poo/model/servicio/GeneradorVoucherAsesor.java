package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.Voucher;
import co.edu.uniquindio.poo.model.VoucherDetallado;

public class GeneradorVoucherAsesor extends GeneradorVoucher {
    @Override
    protected Voucher crearVoucher() {
        return new VoucherDetallado();
    }
}
