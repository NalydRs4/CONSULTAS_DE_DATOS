package com.nomadaware.entities;

// Importaciones necesarias para JPA
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity // Esta clase es una entidad JPA
@Table(name = "tb_cliente") // Mapea la clase a la tabla "tb_cliente" en la base de datos
public class Cliente {

    @Id // Indica la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La id única se genera automáticamente por la base de datos
    @Column(name = "cl_id") // Mapea el campo "id" a la columna "cl_id" en la tabla
    private Integer id;

    @Column(name = "cl_nombre", nullable = false) // Mapea el campo "nombre" a la columna "cl_nombre" en la tabla y no permite valores nulos
    private String nombre;

    @Column(name = "cl_apellido", nullable = false) // Mapea el campo "apellido" a la columna "cl_apellido" en la tabla y no permite valores nulos
    private String apellido;

    @Column(name = "cl_correo") // Mapea el campo "correo" a la columna "cl_correo" en la tabla
    private String correo;

    @Column(name = "cl_telefono") // Mapea el campo "telefono" a la columna "cl_telefono" en la tabla
    private String telefono;

    // Relación bidireccional: Un cliente puede tener muchas facturas
    @OneToMany(mappedBy = "cliente") 
    private List<Factura> facturas;

    public Cliente() {} // Constructor requerido por JPA

    // Constructor para crear un cliente con los datos del cliente
    public Cliente(String nombre, String apellido, String correo, String telefono) { 
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
    }

    // Getters y Setters para los campos de Cliente
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

}