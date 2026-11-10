package com.nomadaware.metodos;

import java.util.List;
import com.nomadaware.entities.Categoria;
import jakarta.persistence.EntityManager;

public class GestionCategorias {

    // Instancia genérica para delegar las operaciones estándar CRUD
    private static final GestionCRUD<Categoria> repo = new GestionCRUD<>(Categoria.class);

    // CREATE - Crear una nueva categoría
    public static Categoria crearCategoria(EntityManager em, String descripcion) {
        Categoria categoria = new Categoria(descripcion);
        repo.persistir(em, categoria);
        return categoria;
    }

    // READ - Obtener todas las categorías
    public static List<Categoria> leerCategorias(EntityManager em) {
        return repo.leerTodos(em);
    }

    // READ - Buscar categoría por ID
    public static Categoria buscarCategoria(EntityManager em, int id) {
        return repo.buscarPorId(em, id);
    }

    // UPDATE - Actualizar una categoría existente
    public static void actualizarCategoria(EntityManager em, Categoria categoria) {
        repo.actualizar(em, categoria);
    }

    // DELETE - Eliminar una categoría por su ID
    public static boolean eliminarCategoria(EntityManager em, int id) {
        return repo.eliminar(em, id);
    }
}