
package co.edu.uniquindio.poo.controller;

import co.edu.uniquindio.poo.app.ContextoApp;
import co.edu.uniquindio.poo.model.Paquete;
import co.edu.uniquindio.poo.model.ProductoTuristico;
import co.edu.uniquindio.poo.model.excepcion.DominioException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;

public class PaquetesController implements ContextoController {
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDescuento;
    @FXML
    private TextField txtComponentes;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Paquete> tblPaquetes;
    @FXML
    private TableColumn<Paquete, String> colCodigo;
    @FXML
    private TableColumn<Paquete, String> colNombre;
    @FXML
    private TableColumn<Paquete, Double> colDescuento;
    @FXML
    private TableColumn<Paquete, Long> colPrecio;
    @FXML
    private TableColumn<Paquete, Integer> colDuracion;
    @FXML
    private TableColumn<Paquete, String> colActividades;

    private ContextoApp contexto;

    @FXML
    private void initialize() {
        limitar(txtCodigo, "[A-Za-z0-9_-]*", 20);
        limitar(txtNombre, "[\\p{L}0-9 .,()\\-]*", 100);
        limitar(txtDescuento, "[0-9]{0,2}(?:[.,][0-9]{0,2})?", 5);
        limitar(txtComponentes, "[A-Za-z0-9_, -]*", 250);
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescuento.setCellValueFactory(new PropertyValueFactory<>("porcentajeDescuento"));
        colPrecio.setCellValueFactory(datos -> new javafx.beans.property.SimpleLongProperty(
                datos.getValue().calcularPrecioPorPersona()).asObject());
        colDuracion.setCellValueFactory(datos -> new javafx.beans.property.SimpleIntegerProperty(
                datos.getValue().calcularDuracionHoras()).asObject());
        colActividades.setCellValueFactory(new PropertyValueFactory<>("actividadesTexto"));
    }

    @Override
    public void setContexto(ContextoApp contexto) {
        this.contexto = contexto;
        refrescar();
    }

    @Override
    public void refrescar() {
        if (contexto != null)
            tblPaquetes.setItems(FXCollections.observableArrayList(contexto.getRegistroPaquetes().listar()));
    }

    @FXML
    private void registrar() {
        try {
            double descuento = Double.parseDouble(txtDescuento.getText().trim().replace(",", "."));
            Paquete paquete = new Paquete(txtCodigo.getText().trim(), txtNombre.getText().trim(), descuento);

            String texto = txtComponentes.getText().trim();
            if (texto.isBlank())
                throw new DominioException("Indique al menos un codigo de actividad o paquete");
            for (String codigo : texto.split(",", -1)) {
                if (codigo.isBlank())
                    throw new DominioException("No deje codigos de componentes vacios");
                ProductoTuristico producto = contexto.buscarProducto(codigo.trim());
                if (producto == null)
                    throw new DominioException("No existe el producto con codigo " + codigo.trim());
                paquete.agregar(producto);
            }

            contexto.getRegistroPaquetes().registrar(paquete);
            limpiar();
            lblMensaje.setText("Paquete registrado correctamente");
            refrescar();
        } catch (NumberFormatException exception) {
            lblMensaje.setText("El descuento debe ser numerico");
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
        txtCodigo.clear();
        txtNombre.clear();
        txtDescuento.clear();
        txtComponentes.clear();
    }
}
