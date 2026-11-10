package com.nomadaware.entities;

// Importaciones ncesarias para JPA
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity // Esta clase es una entidad JPA
@Table(name = "tb_detalle_factura") // Mapea la clase a la tabla "tb_detalle_factura" en la base de datos
public class DetalleFactura {

    @Id // Indica la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La id única se genera automáticamente por la base de datos
    @Column(name = "df_id") // Mapea el campo "id" a la columna "df_id" en la tabla
    private Integer id;

    @Column(name = "df_cantidad", nullable = false) // Mapea el campo "cantidad" a la columna "df_cantidad" en la tabla y no permite valores nulos
    private Integer cantidad;

    @Column(name = "df_precio_unitario", nullable = false) // Mapea el campo "precioUnitario" a la columna "df_precio_unitario" en la tabla y no permite valores nulos
    private BigDecimal precioUnitario;

    @Column(name = "df_subtotal", nullable = false) // Mapea el campo "subtotal" a la columna "df_subtotal" en la tabla y no permite valores nulos
    private BigDecimal subtotal;

    @ManyToOne(optional = false) // Indica una relación de muchos a uno con la entidad Factura y no permite valores nulos
    @JoinColumn(name = "df_factura_id") // Mapea la relación con la columna "df_factura_id" en la tabla
    private Factura factura;

    @ManyToOne(optional = false) // Indica una relación de muchos a uno con la entidad Producto y no permite valores nulos
    @JoinColumn(name = "df_producto_id") // Mapea la relación con la columna "df_producto_id" en la tabla
    private Producto producto;

    public DetalleFactura() {} // Constructor requerido por JPA

    // Constructor para crear un detalle de factura con sus atributos
    public DetalleFactura(Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal, Factura factura,
            Producto producto) { 
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.factura = factura;
        this.producto = producto;
    }

    // Getters y Setters para los campos de DetalleFactura
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}