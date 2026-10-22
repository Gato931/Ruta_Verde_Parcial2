package co.edu.uniquindio.poo.controller;

import co.edu.uniquindio.poo.app.ContextoApp;
import co.edu.uniquindio.poo.model.ResultadoReporte;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

public class ReportesController implements ContextoController {
    @FXML
    private DatePicker dpInicio;
    @FXML
    private DatePicker dpFin;
    @FXML
    private Label lblIngresos;
    @FXML
    private Label lblProducto;
    @FXML
    private Label lblMensaje;

    private ContextoApp contexto;

    @Override
    public void setContexto(ContextoApp contexto) {
        this.contexto = contexto;
        refrescar();
    }

    @Override
    public void refrescar() {
    }

    @FXML
    private void generar() {
        try {
            ResultadoReporte resultado = contexto.getServicioReportes().generar(dpInicio.getValue(), dpFin.getValue());
            lblIngresos.setText("Ingresos del periodo: $" + resultado.getIngresos());
            lblProducto.setText("Producto con mas viajeros: " + resultado.getProductoMasViajeros()
                    + " (" + resultado.getCantidadViajeros() + ")");
            lblMensaje.setText("Reporte generado recorriendo las reservas confirmadas");
        } catch (RuntimeException exception) {
            lblMensaje.setText(exception.getMessage());
        }
    }
}
