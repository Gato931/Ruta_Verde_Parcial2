package co.edu.uniquindio.poo.model.servicio;

import co.edu.uniquindio.poo.model.ResultadoPago;

// Patron estructural ADAPTER: traduce pesos/codigos propios a la API externa PagoAndino.
public class PagoAndinoAdapter implements PasarelaPago {
    private final PagoAndinoAPI api;

    public PagoAndinoAdapter(PagoAndinoAPI api) {
        this.api = api;
    }

    @Override
    public ResultadoPago cobrar(String numeroTarjeta, long valorPesos, String referencia) {
        long valorCentavos = Math.multiplyExact(valorPesos, 100L);
        int codigo = api.procesarCobro(numeroTarjeta, valorCentavos, referencia);

        return switch (codigo) {
            case 0 -> ResultadoPago.aprobado(api.ultimaAutorizacion());
            case 51 -> ResultadoPago.rechazado("Fondos insuficientes");
            case 14 -> ResultadoPago.rechazado("Tarjeta invalida");
            case 91 -> ResultadoPago.rechazado("Pasarela no disponible");
            default -> ResultadoPago.rechazado("Error desconocido de la pasarela: " + codigo);
        };
    }
}
