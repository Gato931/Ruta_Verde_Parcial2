package co.edu.uniquindio.poo.model.servicio;

public class CorreoConsola implements ServicioCorreo {
    @Override
    public void enviar(String destinatario, String asunto, String contenido) {
        System.out.println("\n--- CORREO SIMULADO ---");
        System.out.println("Para: " + destinatario);
        System.out.println("Asunto: " + asunto);
        System.out.println(contenido);
        System.out.println("-----------------------\n");
    }
}
