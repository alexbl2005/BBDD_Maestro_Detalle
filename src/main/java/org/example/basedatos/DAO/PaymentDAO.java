package org.example.basedatos.DAO;

import org.example.basedatos.modelos.payment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                MetodoPago.setCode(resultSet.getString("code"));
                MetodosPagos.add(MetodoPago);
                System.out.println(resultSet.getString("code"));
            }
        }

        return MetodosPagos;
    }
}
