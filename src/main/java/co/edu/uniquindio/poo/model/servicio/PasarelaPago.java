package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.ResultadoPago;

public interface PasarelaPago {
    ResultadoPago cobrar(String numeroTarjeta, long valorPesos, String referencia);
}
