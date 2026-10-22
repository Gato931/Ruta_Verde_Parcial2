
package co.edu.uniquindio.poo.model;

public class VoucherDetallado implements Voucher {
    @Override
    public String generarDetalle(Reserva reserva) {
        StringBuilder texto = new StringBuilder();
        texto.append("Reserva: ").append(reserva.getNumero()).append("\n");
        texto.append("Cliente: ").append(reserva.getCliente().getNombreCompleto()).append("\n");
        texto.append("Producto: ").append(reserva.getProducto().getNombre()).append("\n");
        texto.append("Fecha: ").append(reserva.getFechaViaje()).append("\n");
        texto.append("Viajeros: ").append(reserva.getNumeroViajeros()).append("\n");
        texto.append("Actividades:\n");
        for (Actividad actividad : reserva.getActividadesReservadas()) {
            texto.append(" - ").append(actividad.getNombre())
                    .append(" (").append(actividad.getMunicipio()).append(")\n");
        }
        texto.append("Guia: ").append(reserva.getGuia() == null ? "No incluido" : reserva.getGuia().getNombre())
                .append("\n");
        texto.append("Transporte hotel: ").append(reserva.isTransporteHotel() ? "Si" : "No").append("\n");
        texto.append("Seguro de viaje: ").append(reserva.isSeguroViaje() ? "Si" : "No").append("\n");
        texto.append("Total: $").append(reserva.calcularValorTotal()).append("\n");
        texto.append("Autorizacion: ").append(reserva.getAutorizacionPago());
        return texto.toString();
    }
}
