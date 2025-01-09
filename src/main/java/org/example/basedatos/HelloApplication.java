package org.example.basedatos;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase de la apicación.
 */
public class HelloApplication extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 800, 400);
    scene.getStylesheets().add(getClass().getResource("Estiloo.css").toExternalForm());
    stage.setTitle("Hello!");
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Clase main.
   */
  public static void main(String[] args) {
    launch();
  }
}