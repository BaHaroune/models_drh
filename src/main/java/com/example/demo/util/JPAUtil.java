package com.example.demo.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("default");

    public static EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }
}