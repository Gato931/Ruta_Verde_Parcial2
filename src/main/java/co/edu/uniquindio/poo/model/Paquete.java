
package co.edu.uniquindio.poo.model;

import co.edu.uniquindio.poo.model.excepcion.DominioException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// COMPOSITE: tanto Actividad como Paquete son ProductoTuristico.
public class Paquete implements ProductoTuristico {
    private final String codigo;
    private final String nombre;
    private final double porcentajeDescuento;
    private final List<ProductoTuristico> componentes = new ArrayList<>();
    private boolean bloqueado;

    public Paquete(String codigo, String nombre, double porcentajeDescuento) {
        if (codigo == null || !codigo.matches("[A-Za-z0-9_-]{1,20}")) {
            throw new DominioException("El codigo del paquete solo admite letras, numeros, - y _ (maximo 20)");
        }
        if (nombre == null || !nombre.matches("[\\p{L}0-9 .,()\\-]{2,100}") || nombre.isBlank()) {
            throw new DominioException("El nombre del paquete contiene caracteres no permitidos");
        }
        if (!Double.isFinite(porcentajeDescuento) || porcentajeDescuento < 0 || porcentajeDescuento > 20) {
            throw new DominioException("El descuento debe estar entre 0 y 20 por ciento");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public void agregar(ProductoTuristico producto) {
        if (bloqueado)
            throw new DominioException("No se puede modificar un paquete ya incluido o reservado");
        if (producto == null)
            throw new DominioException("El producto es obligatorio");
        if (producto == this || producto instanceof Paquete paquete && paquete.contienePaquete(this)) {
            throw new DominioException("Un paquete no puede contenerse a si mismo, ni indirectamente");
        }
        for (ProductoTuristico actual : componentes) {
            if (actual.getCodigo().equalsIgnoreCase(producto.getCodigo())) {
                throw new DominioException("Este producto ya esta incluido en el paquete");
            }
        }
        Set<String> codigos = new HashSet<>();
        for (Actividad actividad : obtenerActividades())
            codigos.add(actividad.getCodigo().toLowerCase());
        for (Actividad actividad : producto.obtenerActividades()) {
            if (!codigos.add(actividad.getCodigo().toLowerCase())) {
                throw new DominioException("Actividad repetida dentro del plan: " + actividad.getNombre());
            }
        }
        componentes.add(producto);
        // El subpaquete ya forma parte de otro plan: se evita que luego introduzca
        // duplicados.
        if (producto instanceof Paquete paquete)
            paquete.bloquearCambios();
    }

    public void bloquearCambios() {
        bloqueado = true;
        for (ProductoTuristico producto : componentes) {
            if (producto instanceof Paquete paquete)
                paquete.bloquearCambios();
        }
    }

    public boolean contienePaquete(Paquete buscado) {
        if (this == buscado)
            return true;
        for (ProductoTuristico producto : componentes) {
            if (producto instanceof Paquete paquete && paquete.contienePaquete(buscado))
                return true;
        }
        return false;
    }

    @Override
    public long calcularPrecioPorPersona() {
        long subtotal = 0;
        for (ProductoTuristico producto : componentes) {
            subtotal = Math.addExact(subtotal, producto.calcularPrecioPorPersona());
        }
        return Math.round(subtotal * (1 - porcentajeDescuento / 100.0));
    }

    @Override
    public int calcularDuracionHoras() {
        int horas = 0;
        for (ProductoTuristico producto : componentes) {
            horas = Math.addExact(horas, producto.calcularDuracionHoras());
        }
        return horas;
    }

    @Override
    public List<Actividad> obtenerActividades() {
        List<Actividad> actividades = new ArrayList<>();
        for (ProductoTuristico producto : componentes)
            actividades.addAll(producto.obtenerActividades());
        return actividades;
    }

    public List<ProductoTuristico> getComponentes() {
        return Collections.unmodifiableList(componentes);
    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public String getActividadesTexto() {
        List<String> nombres = new ArrayList<>();
        for (Actividad actividad : obtenerActividades())
            nombres.add(actividad.getNombre());
        return String.join(", ", nombres);
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
