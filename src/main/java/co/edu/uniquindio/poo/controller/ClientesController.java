
package co.edu.uniquindio.poo.controller;

import co.edu.uniquindio.poo.app.ContextoApp;
import co.edu.uniquindio.poo.model.Cliente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientesController implements ContextoController {
    @FXML
    private TextField txtDocumento;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtPais;
    @FXML
    private TextField txtIdioma;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Cliente> tblClientes;
    @FXML
    private TableColumn<Cliente, String> colDocumento;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colCorreo;
    @FXML
    private TableColumn<Cliente, String> colPais;
    @FXML
    private TableColumn<Cliente, String> colIdioma;
    private ContextoApp contexto;

    @FXML
    private void initialize() {
        limitar(txtDocumento, "[A-Za-z0-9]*", 20);
        limitar(txtNombre, "[\\p{L} ]*", 80);
        limitar(txtTelefono, "[0-9]*", 15);
        limitar(txtCorreo, "[A-Za-z0-9@._%+\\-]*", 100);
        limitar(txtPais, "[\\p{L} ]*", 80);
        limitar(txtIdioma, "[\\p{L} ]*", 80);
        colDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colPais.setCellValueFactory(new PropertyValueFactory<>("paisOrigen"));
        colIdioma.setCellValueFactory(new PropertyValueFactory<>("idiomaPreferido"));
    }

    @Override
    public void setContexto(ContextoApp contexto) {
        this.contexto = contexto;
        refrescar();
    }

    @Override
    public void refrescar() {
        if (contexto != null) {
            tblClientes.setItems(FXCollections.observableArrayList(contexto.getRegistroClientes().listar()));
        }
    }

    @FXML
    private void registrar() {
        try {
            Cliente cliente = new Cliente(txtDocumento.getText().trim(), txtNombre.getText().trim(),
                    txtTelefono.getText().trim(), txtCorreo.getText().trim(),
                    txtPais.getText().trim(), txtIdioma.getText().trim());
            contexto.getRegistroClientes().registrar(cliente);
            limpiar();
            lblMensaje.setText("Cliente registrado correctamente");
            refrescar();
        } catch (RuntimeException exception) {
            lblMensaje.setText(exception.getMessage());
        }
    }

    private void limitar(TextField campo, String patron, int maximo) {
        campo.setTextFormatter(new TextFormatter<String>(cambio -> {
            String texto = cambio.getControlNewText();
            return texto.length() <= maximo && texto.matches(patron) ? cambio : null;
        }));
    }

    private void limpiar() {
        txtDocumento.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtPais.clear();
        txtIdioma.clear();
    }
}
