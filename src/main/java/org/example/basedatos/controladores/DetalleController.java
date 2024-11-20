package org.example.basedatos.controladores;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.basedatos.DAO.ConexionDB;
import org.example.basedatos.DAO.PaymentDAO;
import org.example.basedatos.DAO.ProductosDAO;
import org.example.basedatos.HelloApplication;
import org.example.basedatos.modelos.payment;
import org.example.basedatos.modelos.productos;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleController {


    @FXML
    private Button BtnBuscar;
    @FXML
    private TableView tbDatos;
    @FXML
    private TableColumn tcID;
    @FXML
    private TextField tfBusqueda;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnModificar;
    @FXML
    private TableColumn tcID_Factura;
    @FXML
    private TableColumn tcNombre;
    @FXML
    private TableColumn tcCantidad;
    @FXML
    private TableColumn tcPrecio_Total;
    @FXML
    private TableColumn tcEstado;
    @FXML
    private TableColumn tcPrecio_Unitario;

    int id_factura;

    public void RecibirDatos(int idfactura){
        id_factura = idfactura;
    }

    public void initialize() throws SQLException {



        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcID_Factura.setCellValueFactory(new PropertyValueFactory<>("id_factura"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tcPrecio_Unitario.setCellValueFactory(new PropertyValueFactory<>("precio_unitario"));
        tcPrecio_Total.setCellValueFactory(new PropertyValueFactory<>("precio_total"));
        tcEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));


        btnEliminar.disableProperty().bind(tbDatos.getSelectionModel().selectedItemProperty().isNull());
        btnModificar.disableProperty().bind(tbDatos.getSelectionModel().selectedItemProperty().isNull());

    }



    @FXML
    public void Buscar(ActionEvent actionEvent) throws SQLException {

        busqueda();

    }

    @FXML
    public void Eliminar(ActionEvent actionEvent) throws SQLException {
        payment tupla = (payment) tbDatos.getSelectionModel().getSelectedItem();

        int ID = tupla.getId();

        Connection conexion = ConexionDB.getConnection();
        PreparedStatement IDEliminar = conexion.prepareStatement("DELETE FROM payment_method WHERE id = ?;");
        IDEliminar.setInt(1, ID);
        IDEliminar.executeUpdate();

        Alert Eliminado = new Alert(Alert.AlertType.INFORMATION);
        Eliminado.setTitle("Registro eliminido");
        Eliminado.setContentText("Se ha eliminado el registro");
        Eliminado.showAndWait();

        busqueda();
    }

    @FXML
    public void Crear(ActionEvent actionEvent) throws IOException, SQLException {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaCrear.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 350, 200);
            scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Crear");
            stage.setScene(scene);
            stage.showAndWait();

            busqueda();
        }

    @FXML
    public void Modificar(ActionEvent actionEvent) throws IOException, SQLException {

        payment tupla = (payment) tbDatos.getSelectionModel().getSelectedItem();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaModificar.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 200);
        scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
        Stage stage = new Stage();
        stage.setTitle("Modificar");
        stage.setScene(scene);

        ModificarController controller = fxmlLoader.getController();
        controller.RecibirDatos(tupla.getId(), tupla.getCliente(), tupla.isPagado());

        stage.showAndWait();

        busqueda();

    }

    @FXML
    public void DatosClick(MouseEvent event) throws IOException, SQLException {
        if(event.getClickCount()==2)
        {
            payment tupla = (payment) tbDatos.getSelectionModel().getSelectedItem();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaModificar.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 350, 200);
            scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Modificar");
            stage.setScene(scene);

            ModificarController controller = fxmlLoader.getController();
            controller.RecibirDatos(tupla.getId(), tupla.getCliente(), tupla.isPagado());

            stage.showAndWait();

            busqueda();
        }
    }

    public void busqueda() throws SQLException {
        if (tfBusqueda.getText().equals(""))
        {
            Task<Void> tarea = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try{
                        ProductosDAO.RecogerDatos(id_factura);

                        List<productos> Productos = ProductosDAO.obtenerProductos();
                        ObservableList<productos> datos = FXCollections.observableArrayList(Productos);

                        Platform.runLater(() -> {
                            // Actualizar la interfaz gráfica con los valores de nombre y apellido
                            // Por ejemplo, añadirlos a un ListView, Label, etc.
                            tbDatos.setItems(datos);
                        });


                    } catch (SQLException e) {
                        System.err.println("Error de SQL al consultar: " + e.getMessage());
                        Platform.runLater(() -> {

                        });
                    }
                    return null;
                }
            };
            Thread hilo = new Thread(tarea);
            hilo.start();
        }else
        {
            List<payment> Facturas = new ArrayList<>();

            Connection conexion = ConexionDB.getConnection();
            PreparedStatement NombreBuscar = conexion.prepareStatement("SELECT * FROM aafacturas_alejandro WHERE cliente LIKE ?");
            NombreBuscar.setString(1,"%" + tfBusqueda.getText() + "%");
            ResultSet resultado = NombreBuscar.executeQuery();

            while (resultado.next()) {
                payment factura = new payment();
                factura.setId(Integer.valueOf(resultado.getString("id")));
                factura.setCliente(resultado.getString("cliente"));
                factura.setNum_productos(Integer.valueOf(resultado.getString("num_productos")));
                factura.setCreate_date(Timestamp.valueOf(resultado.getString("fecha_creacion")));
                factura.setWrite_date(Timestamp.valueOf(resultado.getString("fecha_modificación")));
                Facturas.add(factura);


            }
            ObservableList<payment> datos = FXCollections.observableArrayList(Facturas);
            tbDatos.setItems(datos);
        }





    }

}


