
package co.edu.uniquindio.poo.controller;

import co.edu.uniquindio.poo.app.ContextoApp;
import co.edu.uniquindio.poo.model.Guia;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class GuiasController implements ContextoController {
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtIdiomas;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtTarifa;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Guia> tblGuias;
    @FXML
    private TableColumn<Guia, String> colId;
    @FXML
    private TableColumn<Guia, String> colNombre;
    @FXML
    private TableColumn<Guia, String> colIdiomas;
    @FXML
    private TableColumn<Guia, String> colTelefono;
    @FXML
    private TableColumn<Guia, Long> colTarifa;

    private ContextoApp contexto;

    @FXML
    private void initialize() {
        limitar(txtId, "[A-Za-z0-9]*", 20);
        limitar(txtNombre, "[\\p{L} ]*", 80);
        limitar(txtIdiomas, "[\\p{L} ,]*", 200);
        limitar(txtTelefono, "[0-9]*", 15);
        limitar(txtTarifa, "[0-9]*", 12);
        colId.setCellValueFactory(new PropertyValueFactory<>("identificacion"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colIdiomas.setCellValueFactory(new PropertyValueFactory<>("idiomasTexto"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colTarifa.setCellValueFactory(new PropertyValueFactory<>("tarifaPorDia"));
    }

    @Override
    public void setContexto(ContextoApp contexto) {
        this.contexto = contexto;
        refrescar();
    }

    @Override
    public void refrescar() {
        if (contexto != null)
            tblGuias.setItems(FXCollections.observableArrayList(contexto.getRegistroGuias().listar()));
    }

    @FXML
    private void registrar() {
        try {
            long tarifa = Long.parseLong(txtTarifa.getText().trim());
            List<String> idiomas = new ArrayList<>();
            for (String idioma : txtIdiomas.getText().split(",", -1)) {
                if (idioma.isBlank())
                    throw new co.edu.uniquindio.poo.model.excepcion.DominioException(
                            "Indique los idiomas separados por coma, sin elementos vacios");
                idiomas.add(idioma.trim());
            }
            Guia guia = new Guia(txtId.getText().trim(), txtNombre.getText().trim(), idiomas,
                    txtTelefono.getText().trim(), tarifa);
            contexto.getRegistroGuias().registrar(guia);
            limpiar();
            lblMensaje.setText("Guia registrado correctamente");
            refrescar();
        } catch (NumberFormatException exception) {
            lblMensaje.setText("La tarifa debe ser un numero entero en pesos");
        } catch (RuntimeException exception) {
            lblMensaje.setText(exception.getMessage());
        }
    }

    // Filtra los caracteres mientras el asesor escribe en el formulario.
    private void limitar(TextField campo, String patron, int maximo) {
        campo.setTextFormatter(new TextFormatter<String>(cambio -> {
            String texto = cambio.getControlNewText();
            return texto.length() <= maximo && texto.matches(patron) ? cambio : null;
        }));
    }

    private void limpiar() {
        txtId.clear();
        txtNombre.clear();
        txtIdiomas.clear();
        txtTelefono.clear();
        txtTarifa.clear();
    }
}
