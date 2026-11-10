package com.nomadaware.metodos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.nomadaware.entities.Cliente;
import com.nomadaware.entities.Factura;
import com.nomadaware.entities.MetodoPago;
import com.nomadaware.entities.Producto;
import com.nomadaware.entities.DetalleFactura;
import jakarta.persistence.EntityManager;

public class GestionFacturas {

    private static final GestionCRUD<Factura> repo = new GestionCRUD<>(Factura.class);

    // CREATE - Crear factura con su detalle
    public static Factura crearFactura(EntityManager em, LocalDate fecha, int clienteId, int metodoPagoId, int productoId, int cantidad) {
        em.getTransaction().begin();
        try {
            Cliente cliente = em.find(Cliente.class, clienteId);
            if (cliente == null) {
                throw new IllegalArgumentException("El cliente con ID " + clienteId + " no existe.");
            }
            MetodoPago metodoPago = em.find(MetodoPago.class, metodoPagoId);
            if (metodoPago == null) {
                throw new IllegalArgumentException("El método de pago con ID " + metodoPagoId + " no existe.");
            }
            Producto producto = em.find(Producto.class, productoId);
            if (producto == null) {
                throw new IllegalArgumentException("El producto con ID " + productoId + " no existe.");
            }

            BigDecimal precioUnitario = producto.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(new BigDecimal(cantidad));

            Factura factura = new Factura(fecha, subtotal, cliente, metodoPago);
            em.persist(factura);

            DetalleFactura detalle = new DetalleFactura(cantidad, precioUnitario, subtotal, factura, producto);
            em.persist(detalle);

            em.getTransaction().commit();
            return factura;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    // READ - Obtener todas las facturas
    public static List<Factura> leerFacturas(EntityManager em) {
        return repo.leerTodos(em);
    }

    // UPDATE - Actualizar una factura existente
    public static void actualizarFactura(EntityManager em, Factura factura) {
        repo.actualizar(em, factura);
    }

    // DELETE - Eliminar una factura por su ID (eliminando primero sus detalles)
    public static boolean eliminarFactura(EntityManager em, int id) {
        em.getTransaction().begin();
        try {
            Factura factura = em.find(Factura.class, id);
            if (factura != null) {
                em.createQuery("DELETE FROM DetalleFactura d WHERE d.factura.id = :facturaId")
                  .setParameter("facturaId", id)
                  .executeUpdate();
                em.remove(factura);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    // Obtener todos los detalles de factura registrados
    public static List<DetalleFactura> leerTodosDetalles(EntityManager em) {
        return em.createQuery("SELECT d FROM DetalleFactura d", DetalleFactura.class)
                 .getResultList();
    }

    // Obtener los detalles asociados a una factura específica
    public static List<DetalleFactura> leerDetallesPorFactura(EntityManager em, int facturaId) {
        String jpql = "SELECT d FROM DetalleFactura d WHERE d.factura.id = :facturaId";
        return em.createQuery(jpql, DetalleFactura.class)
                 .setParameter("facturaId", facturaId)
                 .getResultList();
    }

    // CONSULTA 1: Facturas con total MAYOR o IGUAL a un monto, orden DESCENDENTE y paginación
    public static List<Factura> buscarFacturasPorTotalMayor(EntityManager em, BigDecimal totalMin, int pagina, int tamanoPagina) {
        String jpql = "SELECT f FROM Factura f WHERE f.total >= :totalMin ORDER BY f.total DESC";
        return em.createQuery(jpql, Factura.class)
                 .setParameter("totalMin", totalMin)
                 .setFirstResult((pagina - 1) * tamanoPagina)
                 .setMaxResults(tamanoPagina)
                 .getResultList();
    }
}