package org.example.basedatos.modelos;

/**
 * Clase Productos.
 */
public class Productos {
  private int id;
  private String nombre;
  private int cantidad;
  private int precioUnitario;
  private int precioTotal;
  private String estado;
  private int idFactura;

  public int getIdFactura() {
    return idFactura;
  }

  public void setIdFactura(int idFactura) {
    this.idFactura = idFactura;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public int getPrecioTotal() {
    return precioTotal;
  }

  public void setPrecioTotal(int precioTotal) {
    this.precioTotal = precioTotal;
  }

  public int getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(int precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public int getCantidad() {
    return cantidad;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
