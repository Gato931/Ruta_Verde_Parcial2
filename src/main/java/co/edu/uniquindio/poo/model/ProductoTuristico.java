package co.edu.uniquindio.poo.model;

import java.util.List;

public interface ProductoTuristico {
    String getCodigo();

    String getNombre();

    long calcularPrecioPorPersona();

    int calcularDuracionHoras();

    List<Actividad> obtenerActividades();

    default boolean tieneActividadSuspendida() {
        for (Actividad actividad : obtenerActividades()) {
            if (!actividad.estaDisponible()) {
                return true;
            }
        }
        return false;
    }
}
