
package co.edu.uniquindio.poo.model.repositorio;

import co.edu.uniquindio.poo.model.Actividad;
import co.edu.uniquindio.poo.model.excepcion.DominioException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegistroActividades {
    private final List<Actividad> actividades = new ArrayList<>();
    private final Set<String> codigos;

    public RegistroActividades() {
        this(new HashSet<>());
    }

    public RegistroActividades(Set<String> codigos) {
        this.codigos = codigos;
    }

    public void registrar(Actividad actividad) {
        if (actividad == null)
            throw new DominioException("La actividad es obligatoria");
        String codigo = actividad.getCodigo().toLowerCase();
        if (codigos.contains(codigo))
            throw new DominioException("Ya existe un producto con ese codigo");
        actividades.add(actividad);
        codigos.add(codigo);
    }

    public Actividad buscarPorCodigo(String codigo) {
        if (codigo == null)
            return null;
        for (Actividad actividad : actividades) {
            if (actividad.getCodigo().equalsIgnoreCase(codigo.trim()))
                return actividad;
        }
        return null;
    }

    public List<Actividad> listar() {
        return Collections.unmodifiableList(actividades);
    }
}
