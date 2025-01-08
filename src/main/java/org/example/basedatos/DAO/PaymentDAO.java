package org.example.basedatos.DAO;

import org.example.basedatos.modelos.Factura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    public static List<Factura> obtenerFacturas() throws SQLException {
        List<Factura> Facturas = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConnection();
             Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM aafacturas_alejandro ORDER BY cliente ")) {

            while (resultSet.next()) {
                Factura Factura = new Factura();
                Factura.setId(Integer.valueOf(resultSet.getString("id")));
                Factura.setCliente((resultSet.getString("cliente")));
                Factura.setNumProductos(Integer.valueOf(resultSet.getString("num_productos")));
                Factura.setPagado((resultSet.getBoolean("pagado")));
                Factura.setCreateDate(Timestamp.valueOf(resultSet.getString("fecha_creacion")));
                Factura.setWriteDate(Timestamp.valueOf(resultSet.getString("fecha_modificación")));


                Facturas.add(Factura);
                System.out.println(resultSet.getString("cliente"));
                System.out.println(resultSet.getBoolean("pagado"));
            }
        }

        return Facturas;
    }
}
