package co.edu.uniquindio.poo.model.repositorio;

import co.edu.uniquindio.poo.model.Cliente;
import co.edu.uniquindio.poo.model.excepcion.DominioException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistroClientes {
    private final List<Cliente> clientes = new ArrayList<>();

    public void registrar(Cliente cliente) {
        if (cliente == null)
            throw new DominioException("El cliente es obligatorio");
        if (buscarPorDocumento(cliente.getDocumento()) != null) {
            throw new DominioException("Ya existe un cliente con ese documento");
        }
        clientes.add(cliente);
    }

    public Cliente buscarPorDocumento(String documento) {
        if (documento == null)
            return null;
        for (Cliente cliente : clientes) {
            if (cliente.getDocumento().equalsIgnoreCase(documento.trim()))
                return cliente;
        }
        return null;
    }

    public List<Cliente> listar() {
        return Collections.unmodifiableList(clientes);
    }
}
