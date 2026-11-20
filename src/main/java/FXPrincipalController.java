package com.mycompany.crudjavafx;

import Clases.Estudiante;
import Clases.Usuarios;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FXPrincipalController implements Initializable {

    private static final String SEXO_POR_DEFECTO = "Seleccione sexo";

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidos;
    @FXML
    private ComboBox<String> cbsexo;
    @FXML
    private TableView<Estudiante> tvUsuarios;

    private final Usuarios usuarios = new Usuarios();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        usuarios.mostrarSexoCombo(cbsexo);
        cargarTabla();

        // Al seleccionar una fila, se llenan los campos para poder editar o borrar
        tvUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                txtId.setText(String.valueOf(seleccionado.getId()));
                txtNombre.setText(seleccionado.getNombre());
                txtApellidos.setText(seleccionado.getApellidos());
                cbsexo.setValue(seleccionado.getSexo());
            }
        });
    }

    private void configurarTabla() {
        TableColumn<Estudiante, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()));

        TableColumn<Estudiante, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));

        TableColumn<Estudiante, String> colApellidos = new TableColumn<>("Apellidos");
        colApellidos.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApellidos()));

        TableColumn<Estudiante, String> colSexo = new TableColumn<>("Sexo");
        colSexo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSexo()));

        tvUsuarios.getColumns().addAll(colId, colNombre, colApellidos, colSexo);
        tvUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void cargarTabla() {
        tvUsuarios.setItems(usuarios.obtenerEstudiantes());
    }

    @FXML
    private void guardar() {
        if (!txtId.getText().isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso",
                    "Hay un registro seleccionado. Presiona Limpiar para guardar uno nuevo.");
            return;
        }
        Estudiante nuevo = leerFormulario();
        if (nuevo == null) {
            return;
        }
        if (usuarios.guardarEstudiante(nuevo)) {
            cargarTabla();
            limpiar();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Listo", "Estudiante guardado correctamente.");
        }
    }

    @FXML
    private void editar() {
        if (txtId.getText().isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un registro de la tabla para editarlo.");
            return;
        }
        Estudiante modificado = leerFormulario();
        if (modificado == null) {
            return;
        }
        modificado.setId(Integer.parseInt(txtId.getText()));
        if (usuarios.editarEstudiante(modificado)) {
            cargarTabla();
            limpiar();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Listo", "Estudiante actualizado correctamente.");
        }
    }

    @FXML
    private void borrar() {
        if (txtId.getText().isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un registro de la tabla para borrarlo.");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que quieres borrar a este estudiante?", ButtonType.YES, ButtonType.NO);
        confirmacion.setTitle("Confirmar");
        confirmacion.setHeaderText(null);
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.YES) {
            if (usuarios.eliminarEstudiante(Integer.parseInt(txtId.getText()))) {
                cargarTabla();
                limpiar();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Listo", "Estudiante eliminado correctamente.");
            }
        }
    }

    @FXML
    private void limpiar() {
        txtId.clear();
        txtNombre.clear();
        txtApellidos.clear();
        cbsexo.setValue(SEXO_POR_DEFECTO);
        tvUsuarios.getSelectionModel().clearSelection();
    }

    // Valida los campos y arma un Estudiante; regresa null si algo falta
    private Estudiante leerFormulario() {
        String nombre = txtNombre.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String sexo = cbsexo.getValue();

        if (nombre.isEmpty() || apellidos.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos vacíos", "Escribe el nombre y los apellidos.");
            return null;
        }
        Object idSexo = (sexo == null) ? null : cbsexo.getProperties().get(sexo);
        if (!(idSexo instanceof Integer)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sexo", "Selecciona un sexo de la lista.");
            return null;
        }
        return new Estudiante(0, nombre, apellidos, (Integer) idSexo, sexo);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
