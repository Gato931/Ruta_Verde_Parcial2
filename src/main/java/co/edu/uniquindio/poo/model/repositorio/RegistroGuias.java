package co.edu.uniquindio.poo.model.repositorio;

import co.edu.uniquindio.poo.model.Guia;
import co.edu.uniquindio.poo.model.excepcion.DominioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistroGuias {
    private final List<Guia> guias = new ArrayList<>();

    public void registrar(Guia guia) {
        if (guia == null)
            throw new DominioException("El guia es obligatorio");
        if (buscarPorId(guia.getIdentificacion()) != null) {
            throw new DominioException("Ya existe un guia con esa identificacion");
        }
        guias.add(guia);
    }

    public Guia buscarPorId(String id) {
        if (id == null)
            return null;
        for (Guia guia : guias) {
            if (guia.getIdentificacion().equalsIgnoreCase(id.trim()))
                return guia;
        }
        return null;
    }

    public List<Guia> listar() {
        return Collections.unmodifiableList(guias);
    }
}
