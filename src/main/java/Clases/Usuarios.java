package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

/**
 * Operaciones del CRUD de estudiantes contra la base de datos.
 */
public class Usuarios {

    // Llena el ComboBox con los valores de la tabla sexo
    public void mostrarSexoCombo(ComboBox<String> comboSexo) {
        Conexion objetoConexion = new Conexion();

        comboSexo.getItems().clear();
        comboSexo.setValue("Seleccione sexo");

        String sql = "select * from sexo;";

        try {
            Statement st = objetoConexion.estableceConexion().createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int idSexo = rs.getInt("id");
                String nombreSexo = rs.getString("sexo");

                comboSexo.getItems().add(nombreSexo);
                comboSexo.getProperties().put(nombreSexo, idSexo);
            }
        } catch (Exception e) {
            showAlert("ERROR", "Error al mostrar sexos: " + e.toString());
        } finally {
            objetoConexion.cerrarConexion();
        }
    }

    // READ: regresa todos los estudiantes
    public ObservableList<Estudiante> obtenerEstudiantes() {
        ObservableList<Estudiante> lista = FXCollections.observableArrayList();
        Conexion objetoConexion = new Conexion();

        String sql = "select u.id, u.nombre, u.apellidos, u.fksexo, s.sexo "
                + "from usuarios u inner join sexo s on u.fksexo = s.id "
                + "order by u.id;";

        Connection con = objetoConexion.estableceConexion();
        if (con == null) {
            return lista;
        }

        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getInt("fksexo"),
                        rs.getString("sexo")));
            }
        } catch (SQLException e) {
            showAlert("ERROR", "Error al consultar estudiantes: " + e.getMessage());
        } finally {
            objetoConexion.cerrarConexion();
        }
        return lista;
    }

    // CREATE
    public boolean guardarEstudiante(Estudiante est) {
        String sql = "insert into usuarios (nombre, apellidos, fksexo) values (?, ?, ?);";
        Conexion objetoConexion = new Conexion();

        Connection con = objetoConexion.estableceConexion();
        if (con == null) {
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, est.getNombre());
            ps.setString(2, est.getApellidos());
            ps.setInt(3, est.getIdSexo());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            showAlert("ERROR", "Error al guardar: " + e.getMessage());
            return false;
        } finally {
            objetoConexion.cerrarConexion();
        }
    }

    // UPDATE
    public boolean editarEstudiante(Estudiante est) {
        String sql = "update usuarios set nombre = ?, apellidos = ?, fksexo = ? where id = ?;";
        Conexion objetoConexion = new Conexion();

        Connection con = objetoConexion.estableceConexion();
        if (con == null) {
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, est.getNombre());
            ps.setString(2, est.getApellidos());
            ps.setInt(3, est.getIdSexo());
            ps.setInt(4, est.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            showAlert("ERROR", "Error al editar: " + e.getMessage());
            return false;
        } finally {
            objetoConexion.cerrarConexion();
        }
    }

    // DELETE
    public boolean eliminarEstudiante(int id) {
        String sql = "delete from usuarios where id = ?;";
        Conexion objetoConexion = new Conexion();

        Connection con = objetoConexion.estableceConexion();
        if (con == null) {
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            showAlert("ERROR", "Error al eliminar: " + e.getMessage());
            return false;
        } finally {
            objetoConexion.cerrarConexion();
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
