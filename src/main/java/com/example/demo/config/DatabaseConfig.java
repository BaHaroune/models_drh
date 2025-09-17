package com.example.demo.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class DatabaseConfig {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("defaultPU");

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    // Méthode pour vider le cache JPA/Hibernate
    public static void clearCache() {
        emf.getCache().evictAll();
    }
}

