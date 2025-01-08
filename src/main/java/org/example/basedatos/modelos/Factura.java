package org.example.basedatos.modelos;

import java.sql.Timestamp;

/**
 * Clase Factura.
 */
public class Factura {
  private int id;
  private String cliente;
  private int numProductos;
  private boolean pagado;
  private Timestamp createDate;
  private Timestamp writeDate;

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

  public int getNumProductos() {
    return numProductos;
  }

  public void setNumProductos(int numProductos) {
    this.numProductos = numProductos;
  }

  public boolean isPagado() {
    return pagado;
  }

  public void setPagado(boolean pagado) {
    this.pagado = pagado;
  }

  public Timestamp getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }

  public Timestamp getWriteDate() {
    return writeDate;
  }

  public void setWriteDate(Timestamp writeDate) {
    this.writeDate = writeDate;
  }


}
