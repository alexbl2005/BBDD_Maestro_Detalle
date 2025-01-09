package org.example.basedatos.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.example.basedatos.modelos.Factura;

/**
 * Clase que obtiene los metodos de pago.
 */
public class Paymentdao {

  /**
   * Funcion que recoge todas las facturas de la base de datos.
   */
  public static List<Factura> obtenerFacturas() throws SQLException {
    List<Factura> Facturas = new ArrayList<>();

    try (Connection conexion = Conexiondb.getConnection();
         Statement statement = conexion.createStatement();
         ResultSet resultSet = statement.executeQuery(
             "SELECT * FROM aafacturas_alejandro ORDER BY cliente ")) {

      while (resultSet.next()) {
        Factura factura = new Factura();
        factura.setId(Integer.valueOf(resultSet.getString("id")));
        factura.setCliente((resultSet.getString("cliente")));
        factura.setNumProductos(Integer.valueOf(resultSet.getString("num_productos")));
        factura.setPagado((resultSet.getBoolean("pagado")));
        factura.setCreateDate(Timestamp.valueOf(resultSet.getString("fecha_creacion")));
        factura.setWriteDate(Timestamp.valueOf(resultSet.getString("fecha_modificación")));


        Facturas.add(factura);
        System.out.println(resultSet.getString("cliente"));
        System.out.println(resultSet.getBoolean("pagado"));
      }
    }

    return Facturas;
  }
}
