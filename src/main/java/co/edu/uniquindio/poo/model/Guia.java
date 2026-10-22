
package co.edu.uniquindio.poo.model;

import co.edu.uniquindio.poo.model.excepcion.DominioException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Guia {
    private final String identificacion;
    private final String nombre;
    private final List<String> idiomas;
    private final String telefono;
    private final long tarifaPorDia;

    public Guia(String identificacion, String nombre, List<String> idiomas, String telefono, long tarifaPorDia) {
        if (identificacion == null || !identificacion.matches("[A-Za-z0-9]{3,20}")) {
            throw new DominioException("La identificacion debe tener de 3 a 20 letras o numeros, sin simbolos");
        }
        if (nombre == null || !nombre.matches("[\\p{L} ]{2,80}") || nombre.isBlank()) {
            throw new DominioException("El nombre del guia solo debe contener letras y espacios");
        }
        if (telefono == null || !telefono.matches("[0-9]{7,15}")) {
            throw new DominioException("El telefono debe tener entre 7 y 15 digitos");
        }
        if (tarifaPorDia < 0) {
            throw new DominioException("La tarifa del guia no puede ser negativa");
        }
        if (idiomas == null || idiomas.isEmpty()) {
            throw new DominioException("El guia debe hablar al menos un idioma");
        }
        for (String idioma : idiomas) {
            if (idioma == null || !idioma.matches("[\\p{L} ]{2,80}") || idioma.isBlank()) {
                throw new DominioException("Cada idioma debe contener solo letras y espacios");
            }
        }
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.idiomas = new ArrayList<>(idiomas);
        this.telefono = telefono;
        this.tarifaPorDia = tarifaPorDia;
    }

    public boolean hablaIdioma(String idioma) {
        for (String actual : idiomas) {
            if (actual.equalsIgnoreCase(idioma))
                return true;
        }
        return false;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public List<String> getIdiomas() {
        return Collections.unmodifiableList(idiomas);
    }

    public String getIdiomasTexto() {
        return String.join(", ", idiomas);
    }

    public String getTelefono() {
        return telefono;
    }

    public long getTarifaPorDia() {
        return tarifaPorDia;
    }

    @Override
    public String toString() {
        return identificacion + " - " + nombre;
    }
}
