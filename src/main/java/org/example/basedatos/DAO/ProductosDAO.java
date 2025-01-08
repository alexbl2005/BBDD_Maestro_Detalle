package org.example.basedatos.DAO;

import org.example.basedatos.modelos.Productos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductosDAO {

    static int id_factura;

    public static void RecogerDatos(int idfactura){
        id_factura = idfactura;
    }

    public static List<Productos> obtenerProductos() throws SQLException {
        List<Productos> Productos = new ArrayList<>();
        try (Connection conexion = ConexionDB.getConnection();
             Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM aaproductos_alejandro WHERE id_factura = " + id_factura + " ORDER BY nombre");) {

            while (resultSet.next()) {
                org.example.basedatos.modelos.Productos Producto = new Productos();
                Producto.setId(Integer.valueOf(resultSet.getString("id")));
                Producto.setNombre((resultSet.getString("nombre")));
                Producto.setCantidad(Integer.valueOf(resultSet.getString("cantidad")));
                Producto.setPrecioUnitario(Integer.valueOf(resultSet.getString("precio_unitario")));
                Producto.setPrecioTotal(Integer.valueOf(resultSet.getString("precio_total")));
                Producto.setEstado((resultSet.getString("estado")));
                Producto.setIdFactura(Integer.valueOf(resultSet.getString("id_factura")));

                Productos.add(Producto);
                System.out.println(resultSet.getString("nombre"));
                System.out.println(resultSet.getInt("id_factura"));
            }
        }

        return Productos;
    }
}
