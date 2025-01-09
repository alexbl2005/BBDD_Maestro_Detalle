package org.example.basedatos.controladores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.basedatos.DAO.Conexiondb;

/**
 * Clase que controla la interfaz grafica de cuando creas una nueva factura.
 */
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
  private TextField tfnproductos;

  int nproductos;

  /**
   * Funcion initialize de la clase.
   */
  public void initialize() {
    StringProperty clienteProperty = new SimpleStringProperty();
    StringProperty nproductosProperty = new SimpleStringProperty();
    tfCliente.textProperty().bindBidirectional(clienteProperty);
    tfnproductos.textProperty().bindBidirectional(nproductosProperty);

    btnCrearAceptar.disableProperty().bind(
        Bindings.createBooleanBinding(
            () -> clienteProperty.get() == null || clienteProperty.get().trim().isEmpty()
                || nproductosProperty.get() == null || nproductosProperty.get().trim().isEmpty(),
            clienteProperty, nproductosProperty
        )
    );
  }

  /**
   * Funcion que controla el boton Aceptar.
   */
  @FXML
  public void aceptar(ActionEvent actionEvent) throws SQLException {

    String cliente = tfCliente.getText();
    nproductos = Integer.parseInt(tfnproductos.getText());
    boolean pagado = cbPagado.isSelected();

    Connection conexion = Conexiondb.getConnection();
    PreparedStatement idbuscar = conexion.prepareStatement(
        "INSERT INTO aafacturas_alejandro (cliente, fecha_creacion,  fecha_modificación, pagado,"
            + " num_productos) VALUES (?,NOW(),NOW(),?,?)");
    idbuscar.setString(1, cliente);
    idbuscar.setBoolean(2, pagado);
    idbuscar.setInt(3, nproductos);
    idbuscar.executeUpdate();

    crearProductos();

    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.close();
  }

  /**
   * Funcion que crea los productos automaticamente.
   */
  public void crearProductos() throws SQLException {

    List<Integer> ids = new ArrayList<>();

    try (Connection conexion = Conexiondb.getConnection();
         Statement statement = conexion.createStatement();
         ResultSet resultSet = statement.executeQuery(
             "SELECT id FROM aafacturas_alejandro ORDER BY fecha_creacion DESC LIMIT 1");) {

      while (resultSet.next()) {
        int idCliente = resultSet.getInt("id");
        ids.add(idCliente);
        System.out.println(idCliente);
      }
    }
    do {
      Connection conexion = Conexiondb.getConnection();
      PreparedStatement crearProductos = conexion.prepareStatement(
          "INSERT INTO aaproductos_alejandro (nombre, cantidad,  precio_unitario, precio_total, "
              + "estado, id_factura) VALUES (?,?,?,?,?,?)");
      crearProductos.setString(1, "Nulo");
      crearProductos.setInt(2, 0);
      crearProductos.setInt(3, 0);
      crearProductos.setInt(4, 0);
      crearProductos.setString(5, "Nulo");
      crearProductos.setInt(6, ids.getFirst());
      crearProductos.executeUpdate();
      nproductos--;
    } while (nproductos != 0);

  }

  /**
   * Funcion que controla el boton Cancelar.
   */
  @FXML
  public void cancelar(ActionEvent actionEvent) {

    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.close();
  }
}
