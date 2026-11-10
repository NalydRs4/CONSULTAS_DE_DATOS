package com.nomadaware.entities;

// Importaciones necesarias para JPA
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity // Esta clase es una entidad JPA
@Table(name = "tb_metodo_pago") // Mapea la clase a la tabla "tb_metodo_pago" en la base de datos
public class MetodoPago {

    @Id // Indica la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La id única se genera automáticamente por la base de datos
    @Column(name = "mp_id") // Mapea el campo "id" a la columna "mp_id" en la tabla
    private Integer id;

    // Mapea el nombre del método de pago a la columna "mp_nombre" en la tabla y no permite valores nulos
    @Column(name = "mp_nombre", nullable = false, length = 50) // Mapea el nombre (ej. Efectivo, Tarjeta)
    private String nombre;

    // Mapea la descripción del método de pago a la columna "mp_descripcion" en la tabla y permite valores nulos
    @Column(name = "mp_descripcion", length = 150) // Campo opcional para detalles adicionales
    private String descripcion;

    public MetodoPago() {} // Constructor requerido por JPA

    // Constructor con parámetros para inicializar un método de pago
    public MetodoPago(String nombre, String descripcion) { // Constructor con parámetros
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}