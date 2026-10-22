
package co.edu.uniquindio.poo.controller;

import co.edu.uniquindio.poo.app.ContextoApp;
import co.edu.uniquindio.poo.model.Actividad;
import co.edu.uniquindio.poo.model.Dificultad;
import co.edu.uniquindio.poo.model.EstadoActividad;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;

public class ActividadesController implements ContextoController {
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtMunicipio;
    @FXML
    private TextField txtDuracion;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtCupo;
    @FXML
    private ComboBox<Dificultad> cmbDificultad;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Actividad> tblActividades;
    @FXML
    private TableColumn<Actividad, String> colCodigo;
    @FXML
    private TableColumn<Actividad, String> colNombre;
    @FXML
    private TableColumn<Actividad, String> colMunicipio;
    @FXML
    private TableColumn<Actividad, Integer> colDuracion;
    @FXML
    private TableColumn<Actividad, Long> colPrecio;
    @FXML
    private TableColumn<Actividad, Integer> colCupo;
    @FXML
    private TableColumn<Actividad, Dificultad> colDificultad;
    @FXML
    private TableColumn<Actividad, EstadoActividad> colEstado;

    private ContextoApp contexto;

    @FXML
    private void initialize() {
        limitar(txtCodigo, "[A-Za-z0-9_-]*", 20);
        limitar(txtNombre, "[\\p{L}0-9 .,()\\-]*", 100);
        limitar(txtMunicipio, "[\\p{L} ]*", 80);
        limitar(txtDuracion, "[0-9]*", 4);
        limitar(txtPrecio, "[0-9]*", 12);
        limitar(txtCupo, "[0-9]*", 6);
        cmbDificultad.setItems(FXCollections.observableArrayList(Dificultad.values()));
        cmbDificultad.setValue(Dificultad.BAJA);
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colMunicipio.setCellValueFactory(new PropertyValueFactory<>("municipio"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracionHoras"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioPorPersona"));
        colCupo.setCellValueFactory(new PropertyValueFactory<>("cupoMaximoPorDia"));
        colDificultad.setCellValueFactory(new PropertyValueFactory<>("dificultad"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    @Override
    public void setContexto(ContextoApp contexto) {
        this.contexto = contexto;
        refrescar();
    }

    @Override
    public void refrescar() {
        if (contexto != null)
            tblActividades.setItems(FXCollections.observableArrayList(contexto.getRegistroActividades().listar()));
    }

    @FXML
    private void registrar() {
        try {
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            long precio = Long.parseLong(txtPrecio.getText().trim());
            int cupo = Integer.parseInt(txtCupo.getText().trim());
            Actividad actividad = new Actividad(txtCodigo.getText().trim(), txtNombre.getText().trim(),
                    txtMunicipio.getText().trim(), duracion, precio, cupo,
                    cmbDificultad.getValue(), EstadoActividad.DISPONIBLE);
            contexto.getRegistroActividades().registrar(actividad);
            limpiar();
            lblMensaje.setText("Actividad registrada correctamente");
            refrescar();
        } catch (NumberFormatException exception) {
            lblMensaje.setText("Duracion, precio y cupo deben ser numeros enteros");
        } catch (RuntimeException exception) {
            lblMensaje.setText(exception.getMessage());
        }
    }

    @FXML
    private void cambiarEstado() {
        Actividad seleccionada = tblActividades.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            lblMensaje.setText("Seleccione una actividad");
            return;
        }
        if (seleccionada.getEstado() == EstadoActividad.DISPONIBLE)
            seleccionada.suspender();
        else
            seleccionada.activar();
        lblMensaje.setText("Estado actualizado: " + seleccionada.getEstado());
        refrescar();
    }

    // Filtra los caracteres mientras el asesor escribe en el formulario.
    private void limitar(TextField campo, String patron, int maximo) {
        campo.setTextFormatter(new TextFormatter<String>(cambio -> {
            String texto = cambio.getControlNewText();
            return texto.length() <= maximo && texto.matches(patron) ? cambio : null;
        }));
    }

    private void limpiar() {
        txtCodigo.clear();
        txtNombre.clear();
        txtMunicipio.clear();
        txtDuracion.clear();
        txtPrecio.clear();
        txtCupo.clear();
        cmbDificultad.setValue(Dificultad.BAJA);
    }
}
