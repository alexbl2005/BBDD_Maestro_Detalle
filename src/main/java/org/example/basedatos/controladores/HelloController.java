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
import org.example.basedatos.HelloApplication;
import org.example.basedatos.modelos.Factura;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelloController {


    @FXML
    private Button BtnBuscar;
    @FXML
    private TableView tbDatos;
    @FXML
    private TableColumn tcFechaCreacion;
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
    private TableColumn tcNum_Productos;
    @FXML
    private TableColumn tcCliente;
    @FXML
    private TableColumn tcFechaModificacion;
    @FXML
    private TableColumn tcPagado;

    public void initialize() throws SQLException {



        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        tcNum_Productos.setCellValueFactory(new PropertyValueFactory<>("num_productos"));
        tcPagado.setCellValueFactory(new PropertyValueFactory<>("pagado"));
        tcFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("create_date"));
        tcFechaModificacion.setCellValueFactory(new PropertyValueFactory<>("write_date"));


        btnEliminar.disableProperty().bind(tbDatos.getSelectionModel().selectedItemProperty().isNull());
        btnModificar.disableProperty().bind(tbDatos.getSelectionModel().selectedItemProperty().isNull());
    }

    @FXML
    public void Buscar(ActionEvent actionEvent) throws SQLException {

        busqueda();

    }

    @FXML
    public void Eliminar(ActionEvent actionEvent) throws SQLException {
        Factura tupla = (Factura) tbDatos.getSelectionModel().getSelectedItem();

        int ID = tupla.getId();

        Connection conexion = ConexionDB.getConnection();
        PreparedStatement IDEliminar = conexion.prepareStatement("DELETE FROM aafacturas_alejandro WHERE id = ?;");
        IDEliminar.setInt(1, ID);
        IDEliminar.executeUpdate();

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

        Factura tupla = (Factura) tbDatos.getSelectionModel().getSelectedItem();

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
    public void DatosClick(MouseEvent event) throws IOException, SQLException, InterruptedException {
        if(event.getClickCount()==2)
        {
            Factura tupla = (Factura) tbDatos.getSelectionModel().getSelectedItem();

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaDetalle.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 750, 300);
            scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Productos de la Factura");
            stage.setScene(scene);

            DetalleController controller = fxmlLoader.getController();
            controller.RecibirDatos(tupla.getId(), tupla.getNumProductos());

            stage.show();

            controller.busqueda();
        }
    }

    public void busqueda() throws SQLException {
        if (tfBusqueda.getText().equals(""))
        {
            Task<Void> tarea = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try{
                        List<Factura> Facturas = PaymentDAO.obtenerFacturas();
                        ObservableList<Factura> datos = FXCollections.observableArrayList(Facturas);

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
            List<Factura> Facturas = new ArrayList<>();

            Connection conexion = ConexionDB.getConnection();
            PreparedStatement NombreBuscar = conexion.prepareStatement("SELECT * FROM aafacturas_alejandro WHERE cliente LIKE ?");
            NombreBuscar.setString(1,"%" + tfBusqueda.getText() + "%");
            ResultSet resultado = NombreBuscar.executeQuery();

            while (resultado.next()) {
                Factura factura = new Factura();
                factura.setId(Integer.valueOf(resultado.getString("id")));
                factura.setCliente(resultado.getString("cliente"));
                factura.setNumProductos(Integer.valueOf(resultado.getString("num_productos")));
                factura.setCreateDate(Timestamp.valueOf(resultado.getString("fecha_creacion")));
                factura.setWriteDate(Timestamp.valueOf(resultado.getString("fecha_modificación")));
                Facturas.add(factura);


            }
            ObservableList<Factura> datos = FXCollections.observableArrayList(Facturas);
            tbDatos.setItems(datos);
        }





    }

}


