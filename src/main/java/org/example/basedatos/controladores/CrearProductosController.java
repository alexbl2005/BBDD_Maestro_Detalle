package org.example.basedatos.controladores;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.basedatos.DAO.Conexiondb;

import java.sql.*;

public class CrearProductosController {
    @FXML
    private Button btnCrearCancelar;
    @FXML
    private Button btnCrearAceptar;
    @FXML
    private ChoiceBox cbEstado;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCantidad;
    @FXML
    private TextField tfPrecioUnitario;

    int N_Productos;
    int id_factura;

    public void RecibirDatos(int idfactura, int NProductos){
        id_factura = idfactura;
        N_Productos = NProductos;
    }

    public void initialize(){

        cbEstado.getItems().addAll( "No enviado", "Enviado", "Recibido", "Devuelto");
        cbEstado.setValue("No enviado");

        StringProperty NombreProperty = new SimpleStringProperty();
        tfNombre.textProperty().bindBidirectional(NombreProperty);

        btnCrearAceptar.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> NombreProperty.get() == null || NombreProperty.get().trim().isEmpty(), NombreProperty
                )
        );
    }

    @FXML
    public void Aceptar(ActionEvent actionEvent) throws SQLException {
        String Nombre = tfNombre.getText();
        int Cantidad = Integer.valueOf(tfCantidad.getText());
        int PrecioUnitario = Integer.valueOf(tfPrecioUnitario.getText());
        String Estado = cbEstado.getValue().toString();
        int PrecioTotal = Cantidad * PrecioUnitario;


        Connection conexion = Conexiondb.getConnection();
        PreparedStatement IDBuscar = conexion.prepareStatement("INSERT INTO aaproductos_alejandro (nombre, cantidad, precio_unitario, precio_total, estado, id_factura) VALUES (?,?,?,?,?,?)");
        IDBuscar.setString(1, Nombre);
        IDBuscar.setInt(2, Cantidad);
        IDBuscar.setInt(3, PrecioUnitario);
        IDBuscar.setInt(4, PrecioTotal);
        IDBuscar.setString(5, Estado);
        IDBuscar.setInt(6, id_factura);
        IDBuscar.executeUpdate();

        N_Productos++;

        Connection conexion2 = Conexiondb.getConnection();
        PreparedStatement IDActualizar = conexion.prepareStatement("UPDATE aafacturas_alejandro SET num_productos = ? WHERE id = ?");
        IDActualizar.setInt(1, N_Productos);
        IDActualizar.setInt(2, id_factura);
        IDActualizar.executeUpdate();

        DetalleController.NumeroProductos(N_Productos);

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
