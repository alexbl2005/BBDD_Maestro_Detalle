module org.example.basedatos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires transitive org.checkerframework.checker.qual;


    opens org.example.basedatos to javafx.fxml;
    opens org.example.basedatos.modelos to javafx.base;
    exports org.example.basedatos;
    exports org.example.basedatos.controladores;
    opens org.example.basedatos.controladores to javafx.fxml;
}