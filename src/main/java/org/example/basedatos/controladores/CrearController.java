package org.example.basedatos.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.basedatos.DAO.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CrearController {
    @FXML
    private Button btnCrearCancelar;
    @FXML
    private Button btnCrearAceptar;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCodigo;

    @FXML
    public void Aceptar(ActionEvent actionEvent) throws SQLException {

        String Nombre = tfNombre.getText();
        String Codigo = tfCodigo.getText();

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        String horaFormateada = ahora.format(formatter);

        Connection conexion = ConexionDB.getConnection();
        PreparedStatement IDBuscar = conexion.prepareStatement("INSERT INTO payment_method (name, code,  create_date, write_date) VALUES (?,?,NOW(),NOW())");
        IDBuscar.setString(1, Nombre);
        IDBuscar.setString(2, Codigo);
        IDBuscar.executeUpdate();

        Alert Creado = new Alert(Alert.AlertType.INFORMATION);
        Creado.setTitle("Tarea completada con exito");
        Creado.setContentText("Se ha creado su registro");

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        Creado.showAndWait();
    }

    @FXML
    public void Cancelar(ActionEvent actionEvent) {

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
