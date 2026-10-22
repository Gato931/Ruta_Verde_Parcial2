
package co.edu.uniquindio.poo.model.repositorio;

import co.edu.uniquindio.poo.model.Paquete;
import co.edu.uniquindio.poo.model.excepcion.DominioException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegistroPaquetes {
    private final List<Paquete> paquetes = new ArrayList<>();
    private final Set<String> codigos;

    public RegistroPaquetes() {
        this(new HashSet<>());
    }

    public RegistroPaquetes(Set<String> codigos) {
        this.codigos = codigos;
    }

    public void registrar(Paquete paquete) {
        if (paquete == null)
            throw new DominioException("El paquete es obligatorio");
        if (paquete.obtenerActividades().isEmpty()) {
            throw new DominioException("El paquete debe contener al menos una actividad");
        }
        String codigo = paquete.getCodigo().toLowerCase();
        if (codigos.contains(codigo))
            throw new DominioException("Ya existe un producto con ese codigo");
        paquetes.add(paquete);
        codigos.add(codigo);
    }

    public Paquete buscarPorCodigo(String codigo) {
        if (codigo == null)
            return null;
        for (Paquete paquete : paquetes) {
            if (paquete.getCodigo().equalsIgnoreCase(codigo.trim()))
                return paquete;
        }
        return null;
    }

    public List<Paquete> listar() {
        return Collections.unmodifiableList(paquetes);
    }
}
