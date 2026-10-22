
package co.edu.uniquindio.poo.model;

import co.edu.uniquindio.poo.model.excepcion.DominioException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Actividad implements ProductoTuristico {
    private final String codigo;
    private final String nombre;
    private final String municipio;
    private final int duracionHoras;
    private final long precioPorPersona;
    private final int cupoMaximoPorDia;
    private final Dificultad dificultad;
    private EstadoActividad estado;
    private final Map<LocalDate, Integer> cuposReservados = new HashMap<>();

    public Actividad(String codigo, String nombre, String municipio, int duracionHoras,
            long precioPorPersona, int cupoMaximoPorDia, Dificultad dificultad,
            EstadoActividad estado) {
        if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank()) {
            throw new DominioException("Codigo y nombre de la actividad son obligatorios");
        }
        if (municipio == null || municipio.isBlank()) {
            throw new DominioException("El municipio es obligatorio");
        }
        if (duracionHoras <= 0 || precioPorPersona < 0 || cupoMaximoPorDia <= 0) {
            throw new DominioException("Duracion, precio y cupo de la actividad no son validos");
        }
        if (codigo == null || !codigo.matches("[A-Za-z0-9_-]{1,20}")) {
            throw new DominioException(
                    "El codigo de actividad debe tener de 1 a 20 letras, numeros, guion o guion bajo");
        }
        if (nombre == null || !nombre.matches("[\\p{L}0-9 .,()\\-]{2,100}") || nombre.isBlank()) {
            throw new DominioException("El nombre de actividad contiene caracteres no permitidos");
        }
        if (municipio == null || !municipio.matches("[\\p{L} ]{2,80}") || municipio.isBlank()) {
            throw new DominioException("El municipio debe contener solo letras y espacios (minimo 2 caracteres)");
        }
        if (dificultad == null || estado == null) {
            throw new DominioException("La dificultad y el estado son obligatorios");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.municipio = municipio;
        this.duracionHoras = duracionHoras;
        this.precioPorPersona = precioPorPersona;
        this.cupoMaximoPorDia = cupoMaximoPorDia;
        this.dificultad = dificultad;
        this.estado = estado;
    }

    public boolean hayCupo(LocalDate fecha, int viajeros) {
        int ocupados = cuposReservados.getOrDefault(fecha, 0);
        return ocupados + viajeros <= cupoMaximoPorDia;
    }

    public void reservarCupo(LocalDate fecha, int viajeros) {
        if (!hayCupo(fecha, viajeros)) {
            throw new DominioException("No hay cupo suficiente en la actividad " + nombre);
        }
        cuposReservados.put(fecha, cuposReservados.getOrDefault(fecha, 0) + viajeros);
    }

    public void liberarCupo(LocalDate fecha, int viajeros) {
        int ocupados = cuposReservados.getOrDefault(fecha, 0);
        int nuevo = Math.max(0, ocupados - viajeros);
        if (nuevo == 0) {
            cuposReservados.remove(fecha);
        } else {
            cuposReservados.put(fecha, nuevo);
        }
    }

    public int cuposDisponibles(LocalDate fecha) {
        return cupoMaximoPorDia - cuposReservados.getOrDefault(fecha, 0);
    }

    public boolean estaDisponible() {
        return estado == EstadoActividad.DISPONIBLE;
    }

    public void suspender() {
        estado = EstadoActividad.SUSPENDIDA;
    }

    public void activar() {
        estado = EstadoActividad.DISPONIBLE;
    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public String getMunicipio() {
        return municipio;
    }

    public int getDuracionHoras() {
        return duracionHoras;
    }

    public long getPrecioPorPersona() {
        return precioPorPersona;
    }

    public int getCupoMaximoPorDia() {
        return cupoMaximoPorDia;
    }

    public Dificultad getDificultad() {
        return dificultad;
    }

    public EstadoActividad getEstado() {
        return estado;
    }

    @Override
    public long calcularPrecioPorPersona() {
        return precioPorPersona;
    }

    @Override
    public int calcularDuracionHoras() {
        return duracionHoras;
    }

    @Override
    public List<Actividad> obtenerActividades() {
        return List.of(this);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
