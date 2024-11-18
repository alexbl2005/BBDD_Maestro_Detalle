package org.example.basedatos.DAO;

import org.example.basedatos.modelos.payment;
import org.example.basedatos.modelos.productos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductosDAO {
    public static List<productos> obtenerProductos(int idFactura) throws SQLException {
        List<productos> Productos = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConnection();
             Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM aaproductos_alejandro WHERE id_factura = " + idFactura + " ORDER BY nombre");) {

            while (resultSet.next()) {
                productos Producto = new productos();
                Producto.setId(Integer.valueOf(resultSet.getString("id")));
                Producto.setNombre((resultSet.getString("nombre")));
                Producto.setCantidad(Integer.valueOf(resultSet.getString("cantidad")));
                Producto.setPrecio_unitario(Integer.valueOf(resultSet.getString("precio_unitario")));
                Producto.setPrecio_total(Integer.valueOf(resultSet.getString("precio_total")));
                Producto.setEstado((resultSet.getString("estado")));
                Producto.setId_factura(Integer.valueOf(resultSet.getString("id_factura")));

                Productos.add(Producto);
                System.out.println(resultSet.getString("nombre"));
                System.out.println(resultSet.getInt("id_factura"));
            }
        }

        return Productos;
    }
}
