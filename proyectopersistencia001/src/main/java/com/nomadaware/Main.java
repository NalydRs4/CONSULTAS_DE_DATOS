package com.nomadaware;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UnidadPersistencia");
        EntityManager em = emf.createEntityManager();

        // Iniciar la interfaz gráfica en lugar del menú de consola
        InterfazGUI.iniciar(em);

        //MenuUI.iniciar(em);

        em.close();
        emf.close();
    }
}