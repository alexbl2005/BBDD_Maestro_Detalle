package org.example.basedatos.controladores;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.basedatos.DAO.PaymentDAO;
import org.example.basedatos.modelos.payment;

import java.sql.SQLException;
import java.util.List;

public class HelloController {


    @FXML
    private Button BtnBuscar;
    @FXML
    private TableView tbDatos;
    @FXML
    private TableColumn TCnombre;

    public void initialize() throws SQLException {

        
        TCnombre.setCellValueFactory(new PropertyValueFactory<>("code"));



    }

    @FXML
    public void Buscar(ActionEvent actionEvent) {
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
    }
}

