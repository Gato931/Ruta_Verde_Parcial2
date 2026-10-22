
package co.edu.uniquindio.poo.model.repositorio;

import co.edu.uniquindio.poo.model.EstadoReserva;
import co.edu.uniquindio.poo.model.Reserva;
import co.edu.uniquindio.poo.model.excepcion.DominioException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegistroReservas {
    private final List<Reserva> reservas = new ArrayList<>();

    public void registrar(Reserva reserva) {
        if (reserva == null || reserva.getNumero() == null || reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new DominioException("La reserva debe estar confirmada para registrarla");
        }
        for (Reserva actual : reservas) {
            if (actual == reserva || actual.getNumero().equals(reserva.getNumero())) {
                throw new DominioException("La reserva ya esta registrada");
            }
        }
        reservas.add(reserva);
    }

    public void eliminar(Reserva reserva) {
        reservas.remove(reserva);
    }

    public List<Reserva> listar() {
        return Collections.unmodifiableList(reservas);
    }
}
