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
    private TableColumn tcNombre;
    @FXML
    private TableColumn tcCodigo;
    @FXML
    private TableColumn tcFechaCreacion;
    @FXML
    private TableColumn tcID;
    @FXML
    private TableColumn tcFechaEscritura;
    @FXML
    private ChoiceBox cbBusqueda;
    @FXML
    private TextField tfBusqueda;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnCrear;
    @FXML
    private Button btnModificar;

    public void initialize() throws SQLException {

        cbBusqueda.getItems().addAll( "", "ID", "Nombre", "Codigo");
        cbBusqueda.setOnAction(this::busqueda);
        cbBusqueda.setValue("");

        tcID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcCodigo.setCellValueFactory(new PropertyValueFactory<>("code"));
        tcFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("create_date"));
        tcFechaEscritura.setCellValueFactory(new PropertyValueFactory<>("write_date"));

    }

    private void busqueda(Event event) {
        String busquedacb = (String) cbBusqueda.getValue();
        System.out.println(busquedacb);

    }

    @FXML
    public void Buscar(ActionEvent actionEvent) throws SQLException {

        if (cbBusqueda.getValue().equals(""))
        {
            Task<Void> tarea = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    try{
                        List<payment> payments = PaymentDAO.obtenerBancos();
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

        } else if (cbBusqueda.getValue().equals("ID")) {

            List<payment> MetodosPagos = new ArrayList<>();

            Connection conexion = ConexionDB.getConnection();
            PreparedStatement IDBuscar = conexion.prepareStatement("SELECT * FROM payment_method WHERE id = ?");
            IDBuscar.setInt(1, Integer.valueOf(tfBusqueda.getText()));
            ResultSet resultado = IDBuscar.executeQuery();

                while (resultado.next()) {
                    payment MetodoPago = new payment();
                    MetodoPago.setId(Integer.valueOf(resultado.getString("id")));
                    MetodoPago.setName(resultado.getString("name"));
                    MetodoPago.setCode(resultado.getString("code"));
                    MetodoPago.setCreate_date(Timestamp.valueOf(resultado.getString("create_date")));
                    MetodoPago.setWrite_date(Timestamp.valueOf(resultado.getString("write_date")));
                    MetodosPagos.add(MetodoPago);
                    System.out.println(resultado.getString("code"));

                }
            ObservableList<payment> datos = FXCollections.observableArrayList(MetodosPagos);
                tbDatos.setItems(datos);

        }else if (cbBusqueda.getValue().equals("Nombre")) {

            List<payment> MetodosPagos = new ArrayList<>();

            Connection conexion = ConexionDB.getConnection();
            PreparedStatement NombreBuscar = conexion.prepareStatement("SELECT * FROM payment_method WHERE name LIKE ?");
            NombreBuscar.setString(1,"%" + tfBusqueda.getText() + "%");
            ResultSet resultado = NombreBuscar.executeQuery();

            while (resultado.next()) {
                payment MetodoPago = new payment();
                MetodoPago.setId(Integer.valueOf(resultado.getString("id")));
                MetodoPago.setName(resultado.getString("name"));
                MetodoPago.setCode(resultado.getString("code"));
                MetodoPago.setCreate_date(Timestamp.valueOf(resultado.getString("create_date")));
                MetodoPago.setWrite_date(Timestamp.valueOf(resultado.getString("write_date")));
                MetodosPagos.add(MetodoPago);
                System.out.println(resultado.getString("code"));

            }
            ObservableList<payment> datos = FXCollections.observableArrayList(MetodosPagos);
            tbDatos.setItems(datos);

        }else if (cbBusqueda.getValue().equals("Codigo")) {

            List<payment> MetodosPagos = new ArrayList<>();

            Connection conexion = ConexionDB.getConnection();
            PreparedStatement CodigoBuscar = conexion.prepareStatement("SELECT * FROM payment_method WHERE code LIKE ?");
            CodigoBuscar.setString(1,"%" + tfBusqueda.getText() + "%");
            ResultSet resultado = CodigoBuscar.executeQuery();

            while (resultado.next()) {
                payment MetodoPago = new payment();
                MetodoPago.setId(Integer.valueOf(resultado.getString("id")));
                MetodoPago.setName(resultado.getString("name"));
                MetodoPago.setCode(resultado.getString("code"));
                MetodoPago.setCreate_date(Timestamp.valueOf(resultado.getString("create_date")));
                MetodoPago.setWrite_date(Timestamp.valueOf(resultado.getString("write_date")));
                MetodosPagos.add(MetodoPago);
                System.out.println(resultado.getString("code"));

            }
            ObservableList<payment> datos = FXCollections.observableArrayList(MetodosPagos);
            tbDatos.setItems(datos);
        }
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
    }

    @FXML
    public void Crear(ActionEvent actionEvent) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaCrear.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 350, 200);
            scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
            Stage stage = new Stage();
            stage.setTitle("Crear");
            stage.setScene(scene);
            stage.show();
        }

    @FXML
    public void Modificar(ActionEvent actionEvent) throws IOException {

        payment tupla = (payment) tbDatos.getSelectionModel().getSelectedItem();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaModificar.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 200);
        scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
        Stage stage = new Stage();
        stage.setTitle("Modificar");
        stage.setScene(scene);

        ModificarController controller = fxmlLoader.getController();
        controller.RecibirDatos(tupla.getId(), tupla.getName(), tupla.getCode());

        stage.show();
    }

    @FXML
    public void DatosClick(MouseEvent event) throws IOException {
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
            controller.RecibirDatos(tupla.getId(), tupla.getName(), tupla.getCode());

            stage.show();
        }
    }


}


