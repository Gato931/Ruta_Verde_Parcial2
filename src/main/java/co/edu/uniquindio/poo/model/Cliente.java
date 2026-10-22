
package co.edu.uniquindio.poo.model;

import co.edu.uniquindio.poo.model.excepcion.DominioException;

public class Cliente {
    private final String documento;
    private final String nombreCompleto;
    private final String telefono;
    private final String correo;
    private final String paisOrigen;
    private final String idiomaPreferido;

    public Cliente(String documento, String nombreCompleto, String telefono, String correo,
            String paisOrigen, String idiomaPreferido) {
        if (documento == null || documento.isBlank() || nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new DominioException("Documento y nombre del cliente son obligatorios");
        }
        if (correo == null || correo.isBlank() || telefono == null || telefono.isBlank()) {
            throw new DominioException("Telefono y correo del cliente son obligatorios");
        }
        if (paisOrigen == null || paisOrigen.isBlank() || idiomaPreferido == null || idiomaPreferido.isBlank()) {
            throw new DominioException("Pais e idioma preferido son obligatorios");
        }
        if (documento == null || !documento.matches("[A-Za-z0-9]{3,20}")) {
            throw new DominioException(
                    "El documento o pasaporte debe tener de 3 a 20 letras o numeros, sin simbolos ni espacios");
        }
        if (nombreCompleto == null || !nombreCompleto.matches("[\\p{L} ]{2,80}") || nombreCompleto.isBlank()) {
            throw new DominioException(
                    "El nombre del cliente debe contener solo letras y espacios (minimo 2 caracteres)");
        }
        if (telefono == null || !telefono.matches("[0-9]{7,15}")) {
            throw new DominioException("El telefono debe tener entre 7 y 15 digitos, sin letras ni simbolos");
        }
        if (correo == null || correo.length() > 100 ||
                !correo.matches("[A-Za-z0-9._%+\\-]+@[A-Za-z0-9\\-]+(?:\\.[A-Za-z0-9\\-]+)*\\.[A-Za-z]{2,}")) {
            throw new DominioException("El correo electronico no tiene un formato valido (ejemplo: ana@correo.com)");
        }
        if (paisOrigen == null || !paisOrigen.matches("[\\p{L} ]{2,80}") || paisOrigen.isBlank()) {
            throw new DominioException("El pais de origen debe contener solo letras y espacios (minimo 2 caracteres)");
        }
        if (idiomaPreferido == null || !idiomaPreferido.matches("[\\p{L} ]{2,80}") || idiomaPreferido.isBlank()) {
            throw new DominioException(
                    "El idioma preferido debe contener solo letras y espacios (minimo 2 caracteres)");
        }
        this.documento = documento;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.paisOrigen = paisOrigen;
        this.idiomaPreferido = idiomaPreferido;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public String getIdiomaPreferido() {
        return idiomaPreferido;
    }

    @Override
    public String toString() {
        return documento + " - " + nombreCompleto;
    }
}
