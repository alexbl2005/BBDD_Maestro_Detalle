package org.example.basedatos.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.basedatos.modelos.Productos;

/**
 * Clase que obtiene los productos de la base de datos.
 */
public class Productosdao {

  static int id_factura;

  /**
   * Funcion que se usa para coger datos de otra clase.
   */
  public static void recogerDatos(int idfactura) {
    id_factura = idfactura;
  }

  /**
   *  Funcion que asigna los productos a una lista.
   */
  public static List<Productos> obtenerProductos() throws SQLException {
    List<Productos> productos = new ArrayList<>();
    try (Connection conexion = Conexiondb.getConnection();
         Statement statement = conexion.createStatement();
         ResultSet resultSet = statement.executeQuery(
             "SELECT * FROM aaproductos_alejandro WHERE id_factura = " + id_factura
                 + " ORDER BY nombre");) {

      while (resultSet.next()) {
        org.example.basedatos.modelos.Productos producto = new Productos();
        producto.setId(Integer.valueOf(resultSet.getString("id")));
        producto.setNombre((resultSet.getString("nombre")));
        producto.setCantidad(Integer.valueOf(resultSet.getString("cantidad")));
        producto.setPrecioUnitario(Integer.valueOf(resultSet.getString("precio_unitario")));
        producto.setPrecioTotal(Integer.valueOf(resultSet.getString("precio_total")));
        producto.setEstado((resultSet.getString("estado")));
        producto.setIdFactura(Integer.valueOf(resultSet.getString("id_factura")));

        productos.add(producto);
        System.out.println(resultSet.getString("nombre"));
        System.out.println(resultSet.getInt("id_factura"));
      }
    }

    return productos;
  }
}
