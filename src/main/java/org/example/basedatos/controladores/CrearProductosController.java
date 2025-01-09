package org.example.basedatos.controladores;

import java.sql.*;
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

/**
 * Clase que controla la interfaz grafica de cuando creas un nuevo producto.
 */
public class CrearProductosController {
  @FXML
  private Button btnCrearCancelar;
  @FXML
  private Button btnCrearAceptar;
  @FXML
  private ChoiceBox cbEstado;
  @FXML
  private TextField tfNombre;
  @FXML
  private TextField tfCantidad;
  @FXML
  private TextField tfPrecioUnitario;

  int nproductos;
  int idFactura;

  /**
   * Funcion para coger datos de otra clase.
   */
  public void recibirDatos(int idfactura, int nproductos) {
    idFactura = idfactura;
    this.nproductos = nproductos;
  }

  /**
   * Funcion initialize de la clase.
   */
  public void initialize() {

    cbEstado.getItems().addAll("No enviado", "Enviado", "Recibido", "Devuelto");
    cbEstado.setValue("No enviado");

    StringProperty nombreProperty = new SimpleStringProperty();
    tfNombre.textProperty().bindBidirectional(nombreProperty);

    btnCrearAceptar.disableProperty().bind(
        Bindings.createBooleanBinding(
            () -> nombreProperty.get() == null || nombreProperty.get().trim().isEmpty(),
            nombreProperty
        )
    );
  }

  /**
   * Funcion que controla el boton Crear.
   */
  @FXML
  public void aceptar(ActionEvent actionEvent) throws SQLException {
    String nombre = tfNombre.getText();
    int cantidad = Integer.valueOf(tfCantidad.getText());
    int precioUnitario = Integer.valueOf(tfPrecioUnitario.getText());
    String estado = cbEstado.getValue().toString();
    int precioTotal = cantidad * precioUnitario;


    Connection conexion = Conexiondb.getConnection();
    PreparedStatement idbuscar = conexion.prepareStatement(
        "INSERT INTO aaproductos_alejandro (nombre, cantidad, precio_unitario, precio_total, estado"
            + ", id_factura) VALUES (?,?,?,?,?,?)");
    idbuscar.setString(1, nombre);
    idbuscar.setInt(2, cantidad);
    idbuscar.setInt(3, precioUnitario);
    idbuscar.setInt(4, precioTotal);
    idbuscar.setString(5, estado);
    idbuscar.setInt(6, idFactura);
    idbuscar.executeUpdate();

    nproductos++;

    Connection conexion2 = Conexiondb.getConnection();
    PreparedStatement idActualizar =
        conexion.prepareStatement("UPDATE aafacturas_alejandro SET num_productos = ? WHERE id = ?");
    idActualizar.setInt(1, nproductos);
    idActualizar.setInt(2, idFactura);
    idActualizar.executeUpdate();

    DetalleController.numeroProductos(nproductos);

    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.close();
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
