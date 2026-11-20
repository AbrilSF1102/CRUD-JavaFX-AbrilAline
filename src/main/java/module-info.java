module crudjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.crudjavafx to javafx.fxml;
    exports com.mycompany.crudjavafx;
}
