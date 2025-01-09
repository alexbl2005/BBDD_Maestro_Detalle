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
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.basedatos.DAO.Conexiondb;

/**
 * Clase que controla la interfaz grafica de la ventana Modificar.
 */
public class ModificarController {
  @FXML
  private Button btnCrearCancelar;
  @FXML
  private Button btnCrearAceptar;
  @FXML
  private TextField tfId;
  @FXML
  private CheckBox cbPagado;
  @FXML
  private TextField tfCliente;


  /**
   * Funcion initialize de la clase.
   */
  public void initialize() {
    StringProperty nombreProperty = new SimpleStringProperty();
    tfCliente.textProperty().bindBidirectional(nombreProperty);

    btnCrearAceptar.disableProperty().bind(
        Bindings.createBooleanBinding(
            () -> nombreProperty.get() == null || nombreProperty.get().trim().isEmpty(),
            nombreProperty
        )
    );
  }

  /**
   * Funcion que controla el boton Aceptar.
   */
  @FXML
  public void aceptar(ActionEvent actionEvent) throws SQLException {

    String nombre = tfCliente.getText();
    Boolean pagado = cbPagado.isSelected();
    int id = Integer.parseInt(tfId.getText());

    Connection conexion = Conexiondb.getConnection();
    PreparedStatement idBuscar = conexion.prepareStatement(
        "UPDATE aafacturas_alejandro SET cliente = ?, pagado = ?, fecha_modificación = NOW() WHERE"
            + " id = ?");
    idBuscar.setString(1, nombre);
    idBuscar.setBoolean(2, pagado);
    idBuscar.setInt(3, id);
    idBuscar.executeUpdate();

    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.close();
  }

  /**
   * Funcion para coger datos de otra clase.
   */
  public void recibirDatos(int id, String cliente, boolean pagado) {
    tfId.setText(Integer.toString(id));
    tfCliente.setText(cliente);
    cbPagado.setSelected(pagado);
  }


  /**
   * Funcion que controla el boton cancelar.
   */
  @FXML
  public void cancelar(ActionEvent actionEvent) {

    Node source = (Node) actionEvent.getSource();
    Stage stage = (Stage) source.getScene().getWindow();
    stage.close();
  }
}
