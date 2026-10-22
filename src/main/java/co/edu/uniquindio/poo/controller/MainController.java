package co.edu.uniquindio.poo.controller;

import co.edu.uniquindio.poo.app.ContextoApp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController {
    @FXML
    private TabPane tabPane;
    private ContextoApp contexto;

    public void setContexto(ContextoApp contexto) {
        this.contexto = contexto;
        cargarPestana("Clientes", "clientes-view.fxml");
        cargarPestana("Guias", "guias-view.fxml");
        cargarPestana("Actividades", "actividades-view.fxml");
        cargarPestana("Paquetes", "paquetes-view.fxml");
        cargarPestana("Reservas", "reservas-view.fxml");
        cargarPestana("Reportes", "reportes-view.fxml");
    }

    private void cargarPestana(String titulo, String recurso) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/co/edu/uniquindio/poo/view/" + recurso));
            Node vista = loader.load();
            Object controller = loader.getController();
            if (!(controller instanceof ContextoController contextoController)) {
                throw new IllegalStateException("La vista no implementa ContextoController: " + recurso);
            }
            contextoController.setContexto(contexto);
            Tab tab = new Tab(titulo, vista);
            tab.setClosable(false);
            tab.setOnSelectionChanged(evento -> {
                if (tab.isSelected())
                    contextoController.refrescar();
            });
            tabPane.getTabs().add(tab);
        } catch (Exception exception) {
            throw new IllegalStateException("No fue posible cargar la vista " + recurso, exception);
        }
    }
}
