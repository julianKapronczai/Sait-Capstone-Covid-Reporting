/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.data_access_layer;

import com.sait.capstone.itsd.covidreporting.models.Organisation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.util.List;

/**
 * Provides database access for handling Organisation objects
 * @author Alex Hill
 */
public class OrganisationDB 
{
    /**
     * Used to return a list of all Organisations within the database.
     * @return organisationList
     * @throws Exception 
     */
    public List<Organisation> getAll() throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        
        try
        {
            List<Organisation> allOrganisations = entityManager.createNamedQuery("Organisation.findAll", Organisation.class).getResultList();
            
            return allOrganisations;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to return a list of all Organisations within the database sorted by the specified column.
     * @param columnName
     * @return organisationListSorted
     * @throws Exception 
     */
    public List<Organisation> getAllSorted(String columnName) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        List<Organisation> allOrganisationsSorted = null;
        
        try
        {
            entityTransaction.begin();

            Query customQuery = entityManager.createQuery("SELECT o FROM Organisation o ORDER BY :columnName");
            
            customQuery.setParameter("columnName", columnName);
            
            //Query customQuery = entityManager.createNativeQuery("SELECT c FROM CovidReport c ORDER BY c. ?").setParameter(1, columnName);
            
            System.out.println(customQuery);
            
            allOrganisationsSorted =  customQuery.getResultList();
            
            return allOrganisationsSorted;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to find and return a specific Organisation based on it's unique ID number.
     * @param inputOrganisationId
     * @return organisation
     * @throws Exception 
     */
    public Organisation get (int inputOrganisationId) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        
        try
        {
            Organisation tempOrganisation = entityManager.find(Organisation.class, inputOrganisationId);
            return tempOrganisation;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to insert a new Organisation into the database.
     * @param inputOrganisation
     * @throws Exception 
     */
    public void insert (Organisation inputOrganisation) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.persist(inputOrganisation);
            
            entityTransaction.commit();
        }
        catch (Exception ex)
        {
            entityTransaction.rollback();
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to update an existing Organisation within the database.
     * @param inputOrganisation
     * @throws Exception 
     */
    public void update (Organisation inputOrganisation) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.merge(inputOrganisation);
            
            entityTransaction.commit();
        }
        catch (Exception ex)
        {
            entityTransaction.rollback();
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to delete an Organisation within the database.
     * @param inputOrganisation
     * @throws Exception 
     */
    public void delete (Organisation inputOrganisation) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.remove(inputOrganisation);
            
            entityTransaction.commit();
        }
        catch (Exception ex)
        {
            entityTransaction.rollback();
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to generate the next valid ID for a new Organisation.
     * @return
     * @throws Exception 
     */
    public int getNewOrganisationId () throws Exception
    {
        int newID = -1;
        
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            Query customQuery = entityManager.createQuery("SELECT max(o.organisationId) FROM Organisation o");
            
            newID = (Integer) customQuery.getSingleResult();
            
            newID++;
            
            return newID;
        }
        finally
        {
            entityManager.close();
        }
    }
}
