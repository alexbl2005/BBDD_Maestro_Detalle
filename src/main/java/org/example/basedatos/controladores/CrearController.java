package org.example.basedatos.controladores;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.basedatos.DAO.ConexionDB;
import org.example.basedatos.modelos.productos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

        CrearProductos();

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


    public void CrearProductos() throws SQLException {

        List<Integer> IDs = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConnection();
             Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT id FROM aafacturas_alejandro ORDER BY fecha_creacion DESC LIMIT 1");) {

            while (resultSet.next()) {
                idCliente = resultSet.getInt("id");

                System.out.println(idCliente);
            }
        }

//        PreparedStatement CrearProductos = conexion.prepareStatement("INSERT INTO aaproductos_alejandro (nombre, cantidad,  precio_unitario, precio_total, estado, id_factura) VALUES (?,?,?,?,?,?)");
//        CrearProductos.setString(1, "Nulo");
//        CrearProductos.setInt(2, 0);
//        CrearProductos.setInt(3, 0);
//        CrearProductos.setInt(4, 0);
//        CrearProductos.setString(5, "Nulo");
//        CrearProductos.setInt(6, idCliente);

    }

    @FXML
    public void Cancelar(ActionEvent actionEvent) {

        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
