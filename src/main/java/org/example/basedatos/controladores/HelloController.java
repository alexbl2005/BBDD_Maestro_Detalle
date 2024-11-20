package org.example.basedatos.controladores;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
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

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaDetalle.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 350, 200);
            scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Productos de la Factura");
            stage.setScene(scene);

            DetalleController controller = fxmlLoader.getController();
            controller.RecibirDatos(tupla.getId());

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
                        List<payment> payments = PaymentDAO.obtenerFacturas();
                        ObservableList<payment> datos = FXCollections.observableArrayList(payments);

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


