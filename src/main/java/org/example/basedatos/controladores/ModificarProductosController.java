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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModificarProductosController {
    @FXML
    private Button btnCrearCancelar;
    @FXML
    private Button btnCrearAceptar;
    @FXML
    private TextField tfIDFactura;
    @FXML
    private ChoiceBox cbEstado;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfCantidad;
    @FXML
    private TextField tfPrecioUnitario;
    @FXML
    private TextField tfIDProducto;


    public void initialize(){
        cbEstado.getItems().addAll( "No enviado", "Enviado", "Recibido", "Devuelto");
        cbEstado.setValue("No enviado");

        StringProperty NombreProperty = new SimpleStringProperty();
        tfNombre.textProperty().bindBidirectional(NombreProperty);

        btnCrearAceptar.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> NombreProperty.get() == null || NombreProperty.get().trim().isEmpty(),
                        NombreProperty
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
        int id_factura = Integer.parseInt(tfIDFactura.getText());
        int id = Integer.parseInt(tfIDProducto.getText());

        Connection conexion = Conexiondb.getConnection();
        PreparedStatement IDBuscar = conexion.prepareStatement("UPDATE aaproductos_alejandro SET nombre = ?, cantidad = ?, precio_unitario = ?,precio_total = ?,estado = ?, id_factura = ? WHERE id = ?");
        IDBuscar.setString(1, Nombre);
        IDBuscar.setInt(2, Cantidad);
        IDBuscar.setInt(3, PrecioUnitario);
        IDBuscar.setInt(4, PrecioTotal);
        IDBuscar.setString(5, Estado);
        IDBuscar.setInt(6, id_factura);
        IDBuscar.setInt(7, id);
        IDBuscar.executeUpdate();

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void RecibirDatos(int id_factura, String nombre, int cantidad, int precio_unitario, String estado, int id) {
        tfIDFactura.setText(Integer.toString(id_factura));
        tfNombre.setText(nombre);
        tfCantidad.setText(Integer.toString(cantidad));
        tfPrecioUnitario.setText(Integer.toString(precio_unitario));
        cbEstado.setValue(estado);
        tfIDProducto.setText(Integer.toString(id));
    }

    @FXML
    public void Cancelar(ActionEvent actionEvent) {

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
