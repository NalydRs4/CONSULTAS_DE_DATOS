package com.nomadaware.metodos;

import jakarta.persistence.EntityManager;
import java.util.List;

public class GestionCRUD<T> {

    // Clase genérica para operaciones CRUD básicas
    private final Class<T> entityClass;

    // Constructor que recibe la clase de la entidad para poder realizar operaciones genéricas
    public GestionCRUD(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // Método para persistir una entidad en la base de datos
    public void persistir(EntityManager em, T entity) {
        em.getTransaction().begin();
        try {
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    // Método para buscar una entidad por su ID
    public T buscarPorId(EntityManager em, Object id) {
        return em.find(entityClass, id);
    }

    // Método para leer todas las entidades de un tipo específico
    public List<T> leerTodos(EntityManager em) {
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
    }

    // Método para actualizar una entidad existente
    public void actualizar(EntityManager em, T entity) {
        em.getTransaction().begin();
        try {
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    // Método para eliminar una entidad por su ID
    public boolean eliminar(EntityManager em, Object id) {
        em.getTransaction().begin();
        try {
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
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
}