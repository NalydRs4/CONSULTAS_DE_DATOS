package com.nomadaware.metodos;

import java.math.BigDecimal;
import java.util.List;
import com.nomadaware.entities.Categoria;
import com.nomadaware.entities.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class GestionProductos {
    // Instancia genérica para delegar las operaciones estándar CRUD
    private static final GestionCRUD<Producto> repo = new GestionCRUD<>(Producto.class);

    // CREATE - Crear un nuevo producto
    public static Producto crearProducto(EntityManager em, String nombre, int cantidad, BigDecimal precio,
            String nombreCategoria) {
        em.getTransaction().begin();
        try {
            Categoria categoria;
            try {
                categoria = em.createQuery("SELECT c FROM Categoria c WHERE c.descripcion = :desc", Categoria.class)
                        .setParameter("desc", nombreCategoria)
                        .getSingleResult();
            } catch (NoResultException e) {
                categoria = new Categoria(nombreCategoria);
                em.persist(categoria);
            }
            Producto producto = new Producto(nombre, cantidad, precio, categoria);
            em.persist(producto);
            em.getTransaction().commit();
            return producto;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    // READ - Obtener todos los productos
    public static List<Producto> leerProductos(EntityManager em) {
        return repo.leerTodos(em);
    }

    // UPDATE - Actualizar el precio de un producto
    public static boolean actualizarPrecio(EntityManager em, int id, BigDecimal nuevoPrecio) {
        Producto producto = repo.buscarPorId(em, id);
        if (producto != null) {
            producto.setPrecio(nuevoPrecio);
            repo.actualizar(em, producto);
            return true;
        }
        return false;
    }

    // DELETE - Eliminar un producto por su ID
    public static boolean eliminarProducto(EntityManager em, int id) {
        return repo.eliminar(em, id);
    }

    // CONSULTA 1: Todos los productos, orden DESCENDENTE por precio y paginación
    public static List<Producto> buscarPorPrecioDescendente(EntityManager em, int pagina, int tamanoPagina) {
        String jpql = "SELECT p FROM Producto p ORDER BY p.precio DESC, p.id DESC";
        return em.createQuery(jpql, Producto.class)
                .setFirstResult((pagina - 1) * tamanoPagina)
                .setMaxResults(tamanoPagina)
                .getResultList();
    }

    // CONSULTA 2: Todos los productos, orden ASCENDENTE por precio y paginación
    public static List<Producto> buscarPorPrecioAscendente(EntityManager em, int pagina, int tamanoPagina) {
        String jpql = "SELECT p FROM Producto p ORDER BY p.precio ASC, p.id ASC";
        return em.createQuery(jpql, Producto.class)
                .setFirstResult((pagina - 1) * tamanoPagina)
                .setMaxResults(tamanoPagina)
                .getResultList();
    }

    // CONSULTA 3: Todos los productos de una categoría específica (filto)
    public static List<Producto> buscarPorCategoria(EntityManager em, String nombreCategoria, int pagina,
            int tamanoPagina) {
        String jpql = "SELECT p FROM Producto p WHERE p.categoria.descripcion = :categoria ORDER BY p.id DESC";
        return em.createQuery(jpql, Producto.class)
                .setParameter("categoria", nombreCategoria)
                .setFirstResult((pagina - 1) * tamanoPagina)
                .setMaxResults(tamanoPagina)
                .getResultList();
    }

}