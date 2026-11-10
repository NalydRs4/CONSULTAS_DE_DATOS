package com.nomadaware.metodos;

import java.util.List;

import com.nomadaware.entities.MetodoPago;

import jakarta.persistence.EntityManager;

public class GestionMetodosPago {

    // Instancia genérica para delegar las operaciones estándar CRUD
    private static final GestionCRUD<MetodoPago> repo = new GestionCRUD<>(MetodoPago.class);

    // CREATE - Crear un nuevo método de pago
    public static MetodoPago crearMetodoPago(EntityManager em, String nombre, String descripcion) {
        MetodoPago metodoPago = new MetodoPago(nombre, descripcion);
        repo.persistir(em, metodoPago);
        return metodoPago;
    }

    // READ - Obtener todos los métodos de pago
    public static List<MetodoPago> leerMetodosPago(EntityManager em) {
        return repo.leerTodos(em);
    }

    // DELETE - Eliminar un método de pago
    public static boolean eliminarMetodoPago(EntityManager em, int id) {
        return repo.eliminar(em, id);
    }

    // UPDATE - Actualizar un método de pago existente
    public static void actualizarMetodoPago(EntityManager em, MetodoPago metodoPago) {
        repo.actualizar(em, metodoPago);
    }

}
