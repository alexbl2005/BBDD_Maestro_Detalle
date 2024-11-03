package org.example.basedatos.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.basedatos.DAO.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModificarController {
    @FXML
    private Button btnCrearCancelar;
    @FXML
    private Button btnCrearAceptar;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCodigo;
    @FXML
    private TextField tfID;

    @FXML
    public void Aceptar(ActionEvent actionEvent) throws SQLException {

        String Nombre = tfNombre.getText();
        String Codigo = tfCodigo.getText();
        int ID = Integer.parseInt(tfID.getText());

        Connection conexion = ConexionDB.getConnection();
        PreparedStatement IDBuscar = conexion.prepareStatement("UPDATE payment_method SET name = ?, code = ?, write_date = NOW() WHERE id = ?");
        IDBuscar.setString(1, Nombre);
        IDBuscar.setString(2, Codigo);
        IDBuscar.setInt(3, ID);
        IDBuscar.executeUpdate();

        Alert Creado = new Alert(Alert.AlertType.INFORMATION);
        Creado.setTitle("Tarea completada con exito");
        Creado.setContentText("Se ha modificado su registro");

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        Creado.showAndWait();
    }

    public void RecibirDatos(int id, String name, String code){
        tfID.setText(Integer.toString(id));
        tfNombre.setText(name);
        tfCodigo.setText(code);
    }

    @FXML
    public void Cancelar(ActionEvent actionEvent) {

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
