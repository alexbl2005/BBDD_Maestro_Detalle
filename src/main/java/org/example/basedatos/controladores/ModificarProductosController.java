package org.example.basedatos.controladores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
 * Clase que controla la interfaz grafica que se usa al modificar los productos de una fatura.
 */
public class ModificarProductosController {
  @FXML
  private Button btnCrearCancelar;
  @FXML
  private Button btnCrearAceptar;
  @FXML
  private TextField tfIdfactura;
  @FXML
  private ChoiceBox cbEstado;
  @FXML
  private TextField tfNombre;
  @FXML
  private TextField tfCantidad;
  @FXML
  private TextField tfPrecioUnitario;
  @FXML
  private TextField tfIdproducto;

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
   * Funcion del boton Aceptar.
   */
  @FXML
  public void aceptar(ActionEvent actionEvent) throws SQLException {

    String nombre = tfNombre.getText();
    int cantidad = Integer.valueOf(tfCantidad.getText());
    int precioUnitario = Integer.valueOf(tfPrecioUnitario.getText());
    String estado = cbEstado.getValue().toString();
    int precioTotal = cantidad * precioUnitario;
    int idFactura = Integer.parseInt(tfIdfactura.getText());
    int id = Integer.parseInt(tfIdproducto.getText());

    Connection conexion = Conexiondb.getConnection();
    PreparedStatement idBuscar = conexion.prepareStatement(
        "UPDATE aaproductos_alejandro SET nombre = ?, cantidad = ?, precio_unitario = ?,"
            + "precio_total = ?,estado = ?, id_factura = ? WHERE id = ?");
    idBuscar.setString(1, nombre);
    idBuscar.setInt(2, cantidad);
    idBuscar.setInt(3, precioUnitario);
    idBuscar.setInt(4, precioTotal);
    idBuscar.setString(5, estado);
    idBuscar.setInt(6, idFactura);
    idBuscar.setInt(7, id);
    idBuscar.executeUpdate();

    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.close();
  }

  /**
   * Funcion para pasar datos de una clase a otra.
   */
  public void recibirDatos(int idFactura, String nombre, int cantidad, int precioUnitario,
                           String estado, int id) {
    tfIdfactura.setText(Integer.toString(idFactura));
    tfNombre.setText(nombre);
    tfCantidad.setText(Integer.toString(cantidad));
    tfPrecioUnitario.setText(Integer.toString(precioUnitario));
    cbEstado.setValue(estado);
    tfIdproducto.setText(Integer.toString(id));
  }

  /**
   * FUncion del boton cancelar.
   */
  @FXML
  public void cancelar(ActionEvent actionEvent) {

    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.close();
  }
}
