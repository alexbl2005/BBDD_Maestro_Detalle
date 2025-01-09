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
import org.example.basedatos.DAO.Productosdao;
import org.example.basedatos.HelloApplication;
import org.example.basedatos.modelos.Productos;

/**
 * Clase que controla la ventana principal de los productos de la facturas.
 */
public class DetalleController {


  @FXML
  private Button btnBuscar;
  @FXML
  private TableView tbDatos;
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
  private TableColumn tcidFactura;
  @FXML
  private TableColumn tcNombre;
  @FXML
  private TableColumn tcCantidad;
  @FXML
  private TableColumn tcprecioTotal;
  @FXML
  private TableColumn tcEstado;
  @FXML
  private TableColumn tcprecioUnitario;

  static int N_Productos;
  int idFactura;

  /**
   * Funcion para coger datos de otra clase.
   */
  public void recibirDatos(int idfactura, int nproductos) {
    idFactura = idfactura;
    N_Productos = nproductos;
  }

  /**
   * Funcion para asignar un variable.
   */
  public static void numeroProductos(int nproductos) {
    N_Productos = nproductos;
  }

  /**
   * Funcion initialize de la clase.
   */
  public void initialize() throws SQLException {


    tcid.setCellValueFactory(new PropertyValueFactory<>("id"));
    tcidFactura.setCellValueFactory(new PropertyValueFactory<>("id_factura"));
    tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
    tcprecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precio_unitario"));
    tcprecioTotal.setCellValueFactory(new PropertyValueFactory<>("precio_total"));
    tcEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));


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
   * Funcion que conrola el boton Eliminar.
   */
  @FXML
  public void eliminar(ActionEvent actionEvent) throws SQLException {
    Productos tupla = (Productos) tbDatos.getSelectionModel().getSelectedItem();

    int id = tupla.getId();

    Connection conexion = Conexiondb.getConnection();
    PreparedStatement ideliminar =
        conexion.prepareStatement("DELETE FROM aaproductos_alejandro WHERE id = ?;");
    ideliminar.setInt(1, id);
    ideliminar.executeUpdate();

    N_Productos--;

    Connection conexion2 = Conexiondb.getConnection();
    PreparedStatement idactualizar =
        conexion.prepareStatement("UPDATE aafacturas_alejandro SET num_productos = ? WHERE id = ?");
    idactualizar.setInt(1, N_Productos);
    idactualizar.setInt(2, idFactura);
    idactualizar.executeUpdate();

    busqueda();
  }

  /**
   * Funcion que controla el boton Crear.
   */
  @FXML
  public void crear(ActionEvent actionEvent) throws IOException, SQLException {

    FXMLLoader fxmlLoader =
        new FXMLLoader(HelloApplication.class.getResource("VentanaCrearProductos.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 350, 200);
    scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
    Stage stage = new Stage();
    stage.setTitle("Crear Produto");
    stage.setScene(scene);

    CrearProductosController controller = fxmlLoader.getController();

    controller.recibirDatos(idFactura, N_Productos);

    stage.showAndWait();

    busqueda();
  }

  /**
   * Funcion que controla el boton Modificar.
   */
  @FXML
  public void modificar(ActionEvent actionEvent) throws IOException, SQLException {


    FXMLLoader fxmlLoader =
        new FXMLLoader(HelloApplication.class.getResource("VentanaModificarProductos.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 350, 350);
    scene.getStylesheets().add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
    Stage stage = new Stage();
    stage.setTitle("Modificar");
    stage.setScene(scene);

    Productos tupla = (Productos) tbDatos.getSelectionModel().getSelectedItem();

    ModificarProductosController controller = fxmlLoader.getController();
    controller.recibirDatos(tupla.getIdFactura(), tupla.getNombre(), tupla.getCantidad(),
        tupla.getPrecioUnitario(), tupla.getEstado(), tupla.getId());

    stage.showAndWait();

    busqueda();

  }

  /**
   * Funcion que controla el uso del doble click sobre una tupla para acceder a ella.
   */
  @FXML
  public void datosClick(MouseEvent event) throws IOException, SQLException {
    if (event.getClickCount() == 2) {

      FXMLLoader fxmlLoader =
          new FXMLLoader(HelloApplication.class.getResource("VentanaModificarProductos.fxml"));
      Scene scene = new Scene(fxmlLoader.load(), 350, 350);
      scene.getStylesheets()
          .add(HelloApplication.class.getResource("Estiloo.css").toExternalForm());
      Stage stage = new Stage();
      stage.setTitle("Modificar");
      stage.setScene(scene);

      Productos tupla = (Productos) tbDatos.getSelectionModel().getSelectedItem();

      ModificarProductosController controller = fxmlLoader.getController();
      controller.recibirDatos(tupla.getIdFactura(), tupla.getNombre(), tupla.getCantidad(),
          tupla.getPrecioUnitario(), tupla.getEstado(), tupla.getId());

      stage.showAndWait();

      busqueda();
    }
  }

  /**
   * Funcion que controla el boton busqueda.
   */
  public void busqueda() throws SQLException {
    if (tfBusqueda.getText().equals("")) {
      Task<Void> tarea = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
          try {
            Productosdao.recogerDatos(idFactura);

            List<Productos> productos = Productosdao.obtenerProductos();
            ObservableList<org.example.basedatos.modelos.Productos> datos =
                FXCollections.observableArrayList(productos);

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
      List<Productos> productos = new ArrayList<>();

      Connection conexion = Conexiondb.getConnection();
      PreparedStatement nombreBuscar =
          conexion.prepareStatement("SELECT * FROM aaproductos_alejandro WHERE nombre LIKE ?");
      nombreBuscar.setString(1, "%" + tfBusqueda.getText() + "%");
      ResultSet resultado = nombreBuscar.executeQuery();

      while (resultado.next()) {
        org.example.basedatos.modelos.Productos producto = new Productos();
        producto.setId(Integer.valueOf(resultado.getString("id")));
        producto.setNombre((resultado.getString("nombre")));
        producto.setCantidad(Integer.valueOf(resultado.getString("cantidad")));
        producto.setPrecioUnitario(Integer.valueOf(resultado.getString("precio_unitario")));
        producto.setPrecioTotal(Integer.valueOf(resultado.getString("precio_total")));
        producto.setEstado((resultado.getString("estado")));
        producto.setIdFactura(Integer.valueOf(resultado.getString("id_factura")));

        productos.add(producto);
      }
      ObservableList<org.example.basedatos.modelos.Productos> datos =
          FXCollections.observableArrayList(productos);
      tbDatos.setItems(datos);
    }


  }

}


