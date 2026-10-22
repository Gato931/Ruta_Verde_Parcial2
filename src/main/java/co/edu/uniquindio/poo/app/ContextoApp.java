
package co.edu.uniquindio.poo.app;

import co.edu.uniquindio.poo.model.Agencia;
import co.edu.uniquindio.poo.model.ConsecutivoReserva;
import co.edu.uniquindio.poo.model.ProductoTuristico;
import co.edu.uniquindio.poo.model.repositorio.RegistroActividades;
import co.edu.uniquindio.poo.model.repositorio.RegistroClientes;
import co.edu.uniquindio.poo.model.repositorio.RegistroGuias;
import co.edu.uniquindio.poo.model.repositorio.RegistroPaquetes;
import co.edu.uniquindio.poo.model.repositorio.RegistroReservas;
import co.edu.uniquindio.poo.model.servicio.CorreoConsola;
import co.edu.uniquindio.poo.model.servicio.FachadaConfirmacionReserva;
import co.edu.uniquindio.poo.model.servicio.PagoAndinoAPI;
import co.edu.uniquindio.poo.model.servicio.PagoAndinoAdapter;
import co.edu.uniquindio.poo.model.servicio.ServicioReportes;
import co.edu.uniquindio.poo.model.servicio.ValidadorTarjeta;
import java.util.HashSet;
import java.util.Set;

public class ContextoApp {
    private final Agencia agencia = new Agencia("RutaVerde Quindio", "NIT-DEMO", "RNT-DEMO",
            "Armenia, Quindio", "", "", "");
    private final RegistroClientes registroClientes = new RegistroClientes();
    private final RegistroGuias registroGuias = new RegistroGuias();

    private final Set<String> codigosProductos = new HashSet<>();
    private final RegistroActividades registroActividades = new RegistroActividades(codigosProductos);
    private final RegistroPaquetes registroPaquetes = new RegistroPaquetes(codigosProductos);
    private final RegistroReservas registroReservas = new RegistroReservas();

    private final FachadaConfirmacionReserva fachadaConfirmacionReserva;
    private final ServicioReportes servicioReportes;

    public ContextoApp() {
        fachadaConfirmacionReserva = new FachadaConfirmacionReserva(agencia, registroReservas,
                new ConsecutivoReserva(), new ValidadorTarjeta(),
                new PagoAndinoAdapter(new PagoAndinoAPI()), new CorreoConsola());
        servicioReportes = new ServicioReportes(registroReservas);
    }

    public ProductoTuristico buscarProducto(String codigo) {
        if (codigo == null || codigo.isBlank())
            return null;
        var actividad = registroActividades.buscarPorCodigo(codigo);
        return actividad != null ? actividad : registroPaquetes.buscarPorCodigo(codigo);
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public RegistroClientes getRegistroClientes() {
        return registroClientes;
    }

    public RegistroGuias getRegistroGuias() {
        return registroGuias;
    }

    public RegistroActividades getRegistroActividades() {
        return registroActividades;
    }

    public RegistroPaquetes getRegistroPaquetes() {
        return registroPaquetes;
    }

    public RegistroReservas getRegistroReservas() {
        return registroReservas;
    }

    public FachadaConfirmacionReserva getFachadaConfirmacionReserva() {
        return fachadaConfirmacionReserva;
    }

    public ServicioReportes getServicioReportes() {
        return servicioReportes;
    }
}
