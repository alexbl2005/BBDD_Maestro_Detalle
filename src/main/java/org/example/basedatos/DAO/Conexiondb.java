package org.example.basedatos.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para conectarse a la base de datos.
 */
public class Conexiondb {

  public static Connection connection = null;

  /**
   *  funcion que se conecta la base de datos.
   */
  public static Connection getConnection() {
    // Reemplaza con tus datos de conexión
    String url = "jdbc:postgresql://localhost:5432/odoo";
    String usuario = "odoo";
    String contrasena = "odoo";

    try {
      return DriverManager.getConnection(url, usuario, contrasena);
    } catch (SQLException e) {
      System.err.println("Error de SQL al conectar: " + e.getMessage());
      return null;
    }
  }
}
