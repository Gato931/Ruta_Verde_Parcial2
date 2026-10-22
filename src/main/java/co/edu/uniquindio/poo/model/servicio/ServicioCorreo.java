package co.edu.uniquindio.poo.model.servicio;

public interface ServicioCorreo {
    void enviar(String destinatario, String asunto, String contenido);
}
