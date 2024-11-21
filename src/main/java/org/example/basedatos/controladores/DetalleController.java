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
import org.checkerframework.checker.units.qual.N;
import org.example.basedatos.DAO.ConexionDB;
import org.example.basedatos.DAO.ProductosDAO;
import org.example.basedatos.HelloApplication;
import org.example.basedatos.modelos.productos;

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

    static int N_Productos;
    int id_factura;

    public void RecibirDatos(int idfactura, int NProductos) {
        id_factura = idfactura;
        N_Productos = NProductos;
    }
    public static void NumeroProductos(int NProductos) {
        N_Productos = NProductos;
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
        productos tupla = (productos) tbDatos.getSelectionModel().getSelectedItem();

        int ID = tupla.getId();

        Connection conexion = ConexionDB.getConnection();
        PreparedStatement IDEliminar = conexion.prepareStatement("DELETE FROM aaproductos_alejandro WHERE id = ?;");
        IDEliminar.setInt(1, ID);
        IDEliminar.executeUpdate();

        N_Productos--;

        Connection conexion2 = ConexionDB.getConnection();
        PreparedStatement IDActualizar = conexion.prepareStatement("UPDATE aafacturas_alejandro SET num_productos = ? WHERE id = ?");
        IDActualizar.setInt(1, N_Productos);
        IDActualizar.setInt(2, id_factura);
        IDActualizar.executeUpdate();

        busqueda();
    }

    @FXML
    public void Crear(ActionEvent actionEvent) throws IOException, SQLException {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaCrearProductos.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 350, 200);
            scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Crear Produto");
            stage.setScene(scene);

            CrearProductosController controller = fxmlLoader.getController();

            controller.RecibirDatos(id_factura, N_Productos);

            stage.showAndWait();

            busqueda();
        }

    @FXML
    public void Modificar(ActionEvent actionEvent) throws IOException, SQLException {

        productos tupla = (productos) tbDatos.getSelectionModel().getSelectedItem();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaModificarProductos.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 350);
        scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
        Stage stage = new Stage();
        stage.setTitle("Modificar");
        stage.setScene(scene);

        ModificarProductosController controller = fxmlLoader.getController();
        controller.RecibirDatos(tupla.getId_factura(), tupla.getNombre(), tupla.getCantidad(), tupla.getPrecio_unitario(), tupla.getEstado(), tupla.getId());

        stage.showAndWait();

        busqueda();

    }

    @FXML
    public void DatosClick(MouseEvent event) throws IOException, SQLException {
        if(event.getClickCount()==2)
        {
            productos tupla = (productos) tbDatos.getSelectionModel().getSelectedItem();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaModificarProductos.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 350, 350);
            scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Modificar");
            stage.setScene(scene);

            ModificarProductosController controller = fxmlLoader.getController();
            controller.RecibirDatos(tupla.getId_factura(), tupla.getNombre(), tupla.getCantidad(), tupla.getPrecio_unitario(), tupla.getEstado(), tupla.getId());

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
            List<productos> Productos = new ArrayList<>();

            Connection conexion = ConexionDB.getConnection();
            PreparedStatement NombreBuscar = conexion.prepareStatement("SELECT * FROM aaproductos_alejandro WHERE nombre LIKE ?");
            NombreBuscar.setString(1,"%" + tfBusqueda.getText() + "%");
            ResultSet resultado = NombreBuscar.executeQuery();

            while (resultado.next()) {
                productos Producto = new productos();
                Producto.setId(Integer.valueOf(resultado.getString("id")));
                Producto.setNombre((resultado.getString("nombre")));
                Producto.setCantidad(Integer.valueOf(resultado.getString("cantidad")));
                Producto.setPrecio_unitario(Integer.valueOf(resultado.getString("precio_unitario")));
                Producto.setPrecio_total(Integer.valueOf(resultado.getString("precio_total")));
                Producto.setEstado((resultado.getString("estado")));
                Producto.setId_factura(Integer.valueOf(resultado.getString("id_factura")));

                Productos.add(Producto);
            }
            ObservableList<productos> datos = FXCollections.observableArrayList(Productos);
            tbDatos.setItems(datos);
        }





    }

}


