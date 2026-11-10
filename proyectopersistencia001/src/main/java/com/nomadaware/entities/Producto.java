package com.nomadaware.entities;

// Importaciones necesarias para JPA
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
@Table(name="tb_producto") // Mapea la clase a la tabla "tb_producto" en la base de datos
public class Producto {

    @Id // Indica la clave primaria de la entidad
    @GeneratedValue(strategy=GenerationType.IDENTITY) // La id única se genera automáticamente por la base de datos
    @Column(name="pr_id") // Mapea el campo "id" a la columna "pr_id" en la tabla
    private Integer id;

    @Column(name="pr_nombre", nullable=false) // Mapea el campo "nombre" a la columna "pr_nombre" en la tabla y no permite valores nulos
    private String nombre;

    @Column(name="pr_cantidad") // Mapea el campo "cantidad" a la columna "pr_cantidad" en la tabla
    private Integer cantidad;

    @Column(name="pr_precio") // Mapea el campo "precio" a la columna "pr_precio" en la tabla
    private BigDecimal precio;

    @ManyToOne(optional=true) // Indica una relación de muchos a uno con la entidad Categoria y permite valores nulos
    @JoinColumn(name="pr_categoria_id") // Mapea la relación con la columna "pr_categoria_id" en la tabla
    private Categoria categoria;

    public Producto() {} // Constructor requerido por JPA

    public Producto(String nombre, Integer cantidad, BigDecimal precio, Categoria categoria) { // Constructor para crear un producto con sus atributos
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.categoria = categoria;
    }

    // Getters y Setters para los campos de Producto
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}