package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import javafx.scene.control.Alert;

public class Conexion {

    Connection conectar = null;

    // CAMBIA ESTOS DATOS POR LOS DE TU MYSQL
    String usuario = "root";
    String contrasenia = "TU_CONTRASEÑA";
    String bd = "bdusuarios";
    String ip = "localhost";
    String puerto = "3306";

    String cadena = "jdbc:mysql://" + ip + ":" + puerto + "/" + bd;

    public Connection estableceConexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conectar = DriverManager.getConnection(cadena, usuario, contrasenia);
        } catch (Exception e) {
            showAlert("Mensaje", "No se conectó a la base de datos, error: " + e.toString());
        }
        return conectar;
    }

    public void cerrarConexion() {
        try {
            if (conectar != null && !conectar.isClosed()) {
                conectar.close();
            }
        } catch (Exception e) {
            showAlert("Mensaje", "Error al cerrar conexión: " + e.toString());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
