package Clases;

/**
 * Modelo que representa a un estudiante (una fila de la tabla usuarios).
 */
public class Estudiante {

    private int id;
    private String nombre;
    private String apellidos;
    private int idSexo;
    private String sexo;

    public Estudiante(int id, String nombre, String apellidos, int idSexo, String sexo) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.idSexo = idSexo;
        this.sexo = sexo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getIdSexo() {
        return idSexo;
    }

    public void setIdSexo(int idSexo) {
        this.idSexo = idSexo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
