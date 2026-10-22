
package co.edu.uniquindio.poo.controller;

import co.edu.uniquindio.poo.app.ContextoApp;
import co.edu.uniquindio.poo.model.Cliente;
import co.edu.uniquindio.poo.model.EstadoReserva;
import co.edu.uniquindio.poo.model.Guia;
import co.edu.uniquindio.poo.model.ProductoTuristico;
import co.edu.uniquindio.poo.model.Reserva;
import co.edu.uniquindio.poo.model.ReservaBuilder;
import co.edu.uniquindio.poo.model.ResultadoConfirmacion;
import co.edu.uniquindio.poo.model.excepcion.DominioException;
import co.edu.uniquindio.poo.model.servicio.GeneradorVoucher;
import co.edu.uniquindio.poo.model.servicio.GeneradorVoucherAsesor;
import co.edu.uniquindio.poo.model.servicio.GeneradorVoucherKiosco;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

public class ReservasController implements ContextoController {
    @FXML
    private TextField txtCliente;
    @FXML
    private TextField txtProducto;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField txtViajeros;
    @FXML
    private TextField txtGuia;
    @FXML
    private CheckBox chkTransporte;
    @FXML
    private CheckBox chkSeguro;
    @FXML
    private TextField txtObservaciones;
    @FXML
    private TextField txtTarjeta;
    @FXML
    private ComboBox<String> cmbCanal;
    @FXML
    private Label lblMensaje;
    @FXML
    private TextArea txtVoucher;
    @FXML
    private TableView<Reserva> tblReservas;
    @FXML
    private TableColumn<Reserva, String> colNumero;
    @FXML
    private TableColumn<Reserva, String> colCliente;
    @FXML
    private TableColumn<Reserva, String> colProducto;
    @FXML
    private TableColumn<Reserva, LocalDate> colFecha;
    @FXML
    private TableColumn<Reserva, Integer> colViajeros;
    @FXML
    private TableColumn<Reserva, Long> colTotal;
    @FXML
    private TableColumn<Reserva, EstadoReserva> colEstado;

    private ContextoApp contexto;

    @FXML
    private void initialize() {
        limitar(txtCliente, "[A-Za-z0-9]*", 20);
        limitar(txtProducto, "[A-Za-z0-9_-]*", 20);
        limitar(txtViajeros, "[0-9]*", 2);
        limitar(txtGuia, "[A-Za-z0-9]*", 20);

        txtObservaciones.setTextFormatter(
                new TextFormatter<String>(cambio -> cambio.getControlNewText().length() <= 500 ? cambio : null));
        txtTarjeta.setTextFormatter(new TextFormatter<String>(cambio -> {
            String texto = cambio.getControlNewText();
            int digitos = texto.replace(" ", "").length();
            return texto.matches("[0-9 ]*") && digitos <= 19 && texto.length() <= 27
                    ? cambio
                    : null;
        }));

        dpFecha.setEditable(false);
        dpFecha.setDayCellFactory(calendario -> new DateCell() {
            @Override
            public void updateItem(LocalDate fecha, boolean vacio) {
                super.updateItem(fecha, vacio);
                setDisable(vacio || fecha.isBefore(LocalDate.now()));
            }
        });

        cmbCanal.setItems(FXCollections.observableArrayList("ASESOR", "KIOSCO"));
        cmbCanal.setValue("ASESOR");
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteNombre"));
        colProducto.setCellValueFactory(new PropertyValueFactory<>("productoNombre"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaViaje"));
        colViajeros.setCellValueFactory(new PropertyValueFactory<>("numeroViajeros"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        txtTarjeta.setPromptText("Ejemplo valido: 4539 1488 0343 6467");
    }

    @Override
    public void setContexto(ContextoApp contexto) {
        this.contexto = contexto;
        refrescar();
    }

    @Override
    public void refrescar() {
        if (contexto != null)
            tblReservas.setItems(FXCollections.observableArrayList(contexto.getRegistroReservas().listar()));
    }

    @FXML
    private void confirmar() {
        try {
            Cliente cliente = contexto.getRegistroClientes().buscarPorDocumento(txtCliente.getText().trim());
            if (cliente == null)
                throw new DominioException("No existe el cliente indicado");

            ProductoTuristico producto = contexto.buscarProducto(txtProducto.getText().trim());
            if (producto == null)
                throw new DominioException("No existe la actividad o paquete indicado");

            if (dpFecha.getValue() == null)
                throw new DominioException("Seleccione la fecha del viaje");
            int viajeros = Integer.parseInt(txtViajeros.getText().trim());

            ReservaBuilder builder = new ReservaBuilder(cliente, producto, dpFecha.getValue(), viajeros)
                    .conTransporteHotel(chkTransporte.isSelected())
                    .conSeguroViaje(chkSeguro.isSelected())
                    .conObservaciones(txtObservaciones.getText().trim());

            if (!txtGuia.getText().isBlank()) {
                Guia guia = contexto.getRegistroGuias().buscarPorId(txtGuia.getText().trim());
                if (guia == null)
                    throw new DominioException("No existe el guia indicado");
                builder.conGuia(guia);
            }

            Reserva reserva = builder.construir();
            GeneradorVoucher generador = "KIOSCO".equals(cmbCanal.getValue())
                    ? new GeneradorVoucherKiosco()
                    : new GeneradorVoucherAsesor();

            ResultadoConfirmacion resultado = contexto.getFachadaConfirmacionReserva()
                    .confirmar(reserva, txtTarjeta.getText().trim(), generador);
            lblMensaje.setText(resultado.getMensaje());
            txtVoucher.setText(resultado.getVoucher());

            if (resultado.isExitosa()) {
                limpiar();
                refrescar();
            }
        } catch (NumberFormatException exception) {
            lblMensaje.setText("El numero de viajeros debe ser entero");
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
        txtCliente.clear();
        txtProducto.clear();
        dpFecha.setValue(null);
        txtViajeros.clear();
        txtGuia.clear();
        chkTransporte.setSelected(false);
        chkSeguro.setSelected(false);
        txtObservaciones.clear();
        txtTarjeta.clear();
        cmbCanal.setValue("ASESOR");
    }
}
