package org.example.basedatos.DAO;

import org.example.basedatos.modelos.payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    public static List<payment> obtenerBancos() throws SQLException {
        List<payment> MetodosPagos = new ArrayList<>();

        try (Connection conexion = ConexionDB.getConnection();
             Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM payment_method ORDER BY name ")) {

            while (resultSet.next()) {
                payment MetodoPago = new payment();
                MetodoPago.setId(Integer.valueOf(resultSet.getString("id")));
                MetodoPago.setName(resultSet.getString("name"));
                MetodoPago.setCode(resultSet.getString("code"));
                MetodoPago.setCreate_date(Timestamp.valueOf(resultSet.getString("create_date")));
                MetodoPago.setWrite_date(Timestamp.valueOf(resultSet.getString("write_date")));

                System.out.println(resultSet.getString("code"));
                MetodosPagos.add(MetodoPago);
                System.out.println(resultSet.getString("code"));
            }
        }

        return MetodosPagos;
    }
}
