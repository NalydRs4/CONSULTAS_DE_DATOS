package com.nomadaware.entities;

// Importaciones necesarias para JPA
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity // Esta clase es una entidad JPA
@Table(name = "tb_factura") // Mapea la clase a la tabla "tb_factura" en la base de datos
public class Factura {

    @Id // Indica la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La id única se genera automáticamente por la base de datos
    @Column(name = "fa_id") // Mapea el campo "id" a la columna "fa_id" en la tabla
    private Integer id; 

    @Column(name = "fa_fecha", nullable = false) // Mapea el campo "fecha" a la columna "fa_fecha" en la tabla y no permite valores nulos
    private LocalDate fecha;

    @Column(name = "fa_total") // Mapea el campo "total" a la columna "fa_total" en la tabla
    private BigDecimal total;

    // Relación con la entidad Cliente
    @ManyToOne(optional = true) // Indica que muchas facturas pueden estar asociadas a un cliente.
    @JoinColumn(name = "fa_cliente_id") // Mapea la relación a la columna "fa_cliente_id" en la tabla
    private Cliente cliente;

    // Relación con la entidad MetodoPago
    @ManyToOne(optional = true) // Indica que muchas facturas usan un método de pago. 
    @JoinColumn(name = "fa_metodo_pago_id") // Mapea la relación a la columna en la base de datos
    private MetodoPago metodoPago;

    // Relación con la entidad DetalleFactura
    @OneToMany(mappedBy = "factura") // Indica que una factura puede tener muchos detalles de factura asociados
    private List<DetalleFactura> detalles;

    public Factura() {} // Constructor requerido por JPA

    // Constructor para crear una factura con fecha, total, cliente y método de pago
    public Factura(LocalDate fecha, BigDecimal total, Cliente cliente, MetodoPago metodoPago) { 
        this.fecha = fecha;
        this.total = total;
        this.cliente = cliente;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters para los campos de Factura
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }
    
}