package org.example.basedatos.controladores;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
import org.example.basedatos.DAO.Conexiondb;
import org.example.basedatos.DAO.Paymentdao;
import org.example.basedatos.HelloApplication;
import org.example.basedatos.modelos.Factura;

/**
 * Clase para controlar la ventana principal.
 */
public class HelloController {


  @FXML
  private Button btnBuscar;
  @FXML
  private TableView tbDatos;
  @FXML
  private TableColumn tcFechaCreacion;
  @FXML
  private TableColumn tcid;
  @FXML
  private TextField tfBusqueda;
  @FXML
  private Button btnEliminar;
  @FXML
  private Button btnCrear;
  @FXML
  private Button btnModificar;
  @FXML
  private TableColumn tcnumProductos;
  @FXML
  private TableColumn tcCliente;
  @FXML
  private TableColumn tcFechaModificacion;
  @FXML
  private TableColumn tcPagado;

  /**
   * Funcion initialize de la clase.
   */
  public void initialize() throws SQLException {


    tcid.setCellValueFactory(new PropertyValueFactory<>("id"));
    tcCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
    tcnumProductos.setCellValueFactory(new PropertyValueFactory<>("num_productos"));
    tcPagado.setCellValueFactory(new PropertyValueFactory<>("pagado"));
    tcFechaCreacion.setCellValueFactory(new PropertyValueFactory<>("create_date"));
    tcFechaModificacion.setCellValueFactory(new PropertyValueFactory<>("write_date"));


    btnEliminar.disableProperty().bind(tbDatos.getSelectionModel().selectedItemProperty().isNull());
    btnModificar.disableProperty()
        .bind(tbDatos.getSelectionModel().selectedItemProperty().isNull());
  }


  /**
   * Funcion que controla el boton Buscar.
   */
  @FXML
  public void buscar(ActionEvent actionEvent) throws SQLException {

    busqueda();

  }

  /**
   * Funcion que controla el boton Eliminar.
   */
  @FXML
  public void eliminar(ActionEvent actionEvent) throws SQLException {
    Factura tupla = (Factura) tbDatos.getSelectionModel().getSelectedItem();

    int id = tupla.getId();

    Connection conexion = Conexiondb.getConnection();
    PreparedStatement idEliminar =
        conexion.prepareStatement("DELETE FROM aafacturas_alejandro WHERE id = ?;");
    idEliminar.setInt(1, id);
    idEliminar.executeUpdate();

    busqueda();
  }

  /**
   * Funcion que controla el boton Crear.
   */
  @FXML
  public void crear(ActionEvent actionEvent) throws IOException, SQLException {

    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("VentanaCrear.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 350, 200);
    scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
    Stage stage = new Stage();
    stage.setTitle("Crear");
    stage.setScene(scene);
    stage.showAndWait();

    busqueda();
  }

  /**
   * Funcion que controla el boton Modificar.
   */
  @FXML
  public void modificar(ActionEvent actionEvent) throws IOException, SQLException {

    FXMLLoader fxmlLoader =
        new FXMLLoader(HelloApplication.class.getResource("VentanaModificar.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 350, 200);
    scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
    Stage stage = new Stage();
    stage.setTitle("Modificar");
    stage.setScene(scene);

    Factura tupla = (Factura) tbDatos.getSelectionModel().getSelectedItem();

    ModificarController controller = fxmlLoader.getController();
    controller.recibirDatos(tupla.getId(), tupla.getCliente(), tupla.isPagado());

    stage.showAndWait();

    busqueda();

  }

  /**
   * Funcion que controla el uso del doble click sobre una tupla para acceder a ella.
   */
  @FXML
  public void datosClick(MouseEvent event) throws IOException, SQLException, InterruptedException {
    if (event.getClickCount() == 2) {

      FXMLLoader fxmlLoader =
          new FXMLLoader(HelloApplication.class.getResource("VentanaDetalle.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 750, 300);
      scene.getStylesheets()
          .add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
      Stage stage = new Stage();
      stage.setTitle("Productos de la Factura");
      stage.setScene(scene);

      Factura tupla = (Factura) tbDatos.getSelectionModel().getSelectedItem();

      DetalleController controller = fxmlLoader.getController();
      controller.recibirDatos(tupla.getId(), tupla.getNumProductos());

      stage.show();

      controller.busqueda();
    }
  }

  /**
   * Funcion que contola la barra de busqueda.
   */
  public void busqueda() throws SQLException {
    if (tfBusqueda.getText().equals("")) {
      Task<Void> tarea = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          try {
            List<Factura> Facturas = Paymentdao.obtenerFacturas();
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
    } else {
      List<Factura> Facturas = new ArrayList<>();

      Connection conexion = Conexiondb.getConnection();
      PreparedStatement nombreBuscar =
          conexion.prepareStatement("SELECT * FROM aafacturas_alejandro WHERE cliente LIKE ?");
      nombreBuscar.setString(1, "%" + tfBusqueda.getText() + "%");
      ResultSet resultado = nombreBuscar.executeQuery();

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


