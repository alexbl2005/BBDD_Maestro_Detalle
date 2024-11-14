package org.example.basedatos.modelos;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;

public class payment {
    private int id;
    private String cliente;
    private int num_productos;
    private boolean pagado;
    private Timestamp create_date;
    private Timestamp write_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getNum_productos() {
        return num_productos;
    }

    public void setNum_productos(int num_productos) {
        this.num_productos = num_productos;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public Timestamp getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Timestamp create_date) {
        this.create_date = create_date;
    }

    public Timestamp getWrite_date() {
        return write_date;
    }

    public void setWrite_date(Timestamp write_date) {
        this.write_date = write_date;
    }




}
