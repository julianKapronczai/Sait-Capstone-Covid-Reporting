/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.data_access_layer;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Provides direct database access for other xxxDB classes
 * @author Alex Hill
 */
public class DBUtil
{
    private static final EntityManagerFactory entityFactory = Persistence.createEntityManagerFactory("CovidReporting_PU");
    
    /**
     * Returns the entity manager factory, ensuring there is only a single EMF at any one time in accordance with the singleton pattern.
     * @return entityFactory
     */
    public static EntityManagerFactory getEntityFactory() 
    {
        return entityFactory;
    }
}
