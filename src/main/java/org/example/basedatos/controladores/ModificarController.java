package org.example.basedatos.controladores;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    private TextField tfID;
    @FXML
    private CheckBox cbPagado;
    @FXML
    private TextField tfCliente;

    public void initialize(){
        StringProperty NombreProperty = new SimpleStringProperty();
        tfCliente.textProperty().bindBidirectional(NombreProperty);

        btnCrearAceptar.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> NombreProperty.get() == null || NombreProperty.get().trim().isEmpty(),
                        NombreProperty
                )
        );
    }

    @FXML
    public void Aceptar(ActionEvent actionEvent) throws SQLException {

        String Nombre = tfCliente.getText();
        Boolean Pagado = cbPagado.isSelected();
        int ID = Integer.parseInt(tfID.getText());

        Connection conexion = ConexionDB.getConnection();
        PreparedStatement IDBuscar = conexion.prepareStatement("UPDATE aafacturas_alejandro SET cliente = ?, pagado = ?, fecha_modificación = NOW() WHERE id = ?");
        IDBuscar.setString(1, Nombre);
        IDBuscar.setBoolean(2, Pagado);
        IDBuscar.setInt(3, ID);
        IDBuscar.executeUpdate();

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void RecibirDatos(int id, String cliente, boolean pagado){
        tfID.setText(Integer.toString(id));
        tfCliente.setText(cliente);
        cbPagado.setSelected(pagado);
    }

    @FXML
    public void Cancelar(ActionEvent actionEvent) {

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
