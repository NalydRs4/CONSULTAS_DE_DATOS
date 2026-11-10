package com.nomadaware.metodos;

import java.util.List;
import com.nomadaware.entities.Cliente;
import jakarta.persistence.EntityManager;

public class GestionClientes {
    
    // Instancia genérica para delegar las operaciones estándar CRUD
    private static final GestionCRUD<Cliente> repo = new GestionCRUD<>(Cliente.class);

    // CREATE - Crear un nuevo cliente
    public static Cliente crearCliente(EntityManager em, String nombre, String apellido, String correo, String telefono) {
        // Se utiliza el constructor con parámetros definido en Cliente.java
        Cliente cliente = new Cliente(nombre, apellido, correo, telefono);
        // Delega la persistencia al método persistir de GestionCRUD
        repo.persistir(em, cliente);
        return cliente;
    }

    // READ - Obtener todos los clientes
    public static List<Cliente> leerClientes(EntityManager em) {
        return repo.leerTodos(em);
    }
    
    // READ - Buscar cliente por ID
    public static Cliente buscarCliente(EntityManager em, int id) {
        return repo.buscarPorId(em, id);
    }

    // UPDATE - Actualizar un cliente existente
    public static void actualizarCliente(EntityManager em, Cliente cliente) {
        // Delega al método actualizar de GestionCRUD, el cual hace el merge
        repo.actualizar(em, cliente);
    }

    // DELETE - Eliminar un cliente por su ID
    public static boolean eliminarCliente(EntityManager em, int id) {
        // Delega al método eliminar de GestionCRUD, que busca y remueve si existe
        return repo.eliminar(em, id);
    }

    // CONSULTA 1: Buscar clientes por apellido
    public static List<Cliente> buscarClientesPorApellido(EntityManager em, String apellido) {
        return em.createQuery("SELECT c FROM Cliente c WHERE c.apellido = :apellido", Cliente.class)
                 .setParameter("apellido", apellido)
                 .getResultList();
    }

}