package com.nomadaware.entities;

// Importaciones necesarias para JPA 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity // Esta clase es una entidad JPA
@Table(name = "tb_categoria") // Mapea la clase a la tabla "tb_categoria" en la base de datos
public class Categoria {

    @Id // Indica la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La id única se genera automáticamente por la base de datos
    @Column(name = "cat_id") // Mapea el campo "id" a la columna "cat_id" en la tabla
    private Integer id; 

    @Column(name = "cat_descripcion", nullable = false) // Mapea el campo "descripcion" a la columna "cat_descripcion" en la tabla y no permite valores nulos
    private String descripcion;

    public Categoria() {} // Constructor requerido por JPA

    public Categoria(String descripcion) { // Constructor para crear una categoría con una descripción
        this.descripcion = descripcion;
    }

    // Getters y Setters para los campos de Categoria
    public Integer getId() { 
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}