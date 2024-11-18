package org.example.basedatos.controladores;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.checkerframework.checker.units.qual.N;
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
    private CheckBox cbPagado;
    @FXML
    private TextField tfCliente;
    @FXML
    private TextField tfNProductos;


    public void initialize(){
        StringProperty ClienteProperty = new SimpleStringProperty();
        StringProperty NProductosProperty = new SimpleStringProperty();
        tfCliente.textProperty().bindBidirectional(ClienteProperty);
        tfNProductos.textProperty().bindBidirectional(NProductosProperty);

        btnCrearAceptar.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> ClienteProperty.get() == null || ClienteProperty.get().trim().isEmpty() ||
                                NProductosProperty.get() == null || NProductosProperty.get().trim().isEmpty(),
                        ClienteProperty, NProductosProperty
                )
        );
    }

    @FXML
    public void Aceptar(ActionEvent actionEvent) throws SQLException {

        String Cliente = tfCliente.getText();
        int NProductos = Integer.parseInt(tfNProductos.getText());
        boolean pagado = cbPagado.isSelected();

        Connection conexion = ConexionDB.getConnection();
        PreparedStatement IDBuscar = conexion.prepareStatement("INSERT INTO aafacturas_alejandro (cliente, fecha_creacion,  fecha_modificación, pagado, num_productos) VALUES (?,NOW(),NOW(),?,?)");
        IDBuscar.setString(1, Cliente);
        IDBuscar.setBoolean(2, pagado);
        IDBuscar.setInt(3, NProductos);
        IDBuscar.executeUpdate();

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void Cancelar(ActionEvent actionEvent) {

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
