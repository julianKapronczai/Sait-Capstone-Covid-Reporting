/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.data_access_layer;

import com.sait.capstone.itsd.covidreporting.models.UuidMap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

/**
 * Provides database access for handling UuidMap objects
 * @author Alex Hill
 */
public class UuidDB
{
    /**
     * Used to find the username associated with the specified UUID.
     * @param uuidMapId 
     * @return a UuidMap object 
     * @throws Exception 
     */
    public UuidMap get(int uuidMapId) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        
        try
        {
            UuidMap tempUuidMap = entityManager.find(UuidMap.class, uuidMapId);
            return tempUuidMap;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to find the UuidMap associated with a specific userId
     * @param uuidMapId 
     * @return a UuidMap object 
     * @throws Exception 
     */
    public UuidMap getByUserID(int userId) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        UuidMap uuidMap = null;
         try
        {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            Query customQuery = entityManager.createQuery("SELECT u FROM UuidMap u WHERE u.userid = :userid");
            
            customQuery.setParameter("userid", userId);
            
            uuidMap = (UuidMap) customQuery.getSingleResult();
            
            return uuidMap;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to fetch the UuidMap with by a specific UUID.
     * @param inputUuid 
     * @return a UuidMap object 
     * @throws Exception 
     */
    public UuidMap getByUuid(String inputUuid) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        UuidMap uuidMap = null;
         try
        {
            EntityTransaction entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            Query customQuery = entityManager.createQuery("SELECT u FROM UuidMap u WHERE u.uuid = :uuid");
            
            customQuery.setParameter("uuid", inputUuid);
            
            uuidMap = (UuidMap) customQuery.getSingleResult();
            
            return uuidMap;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to insert a new UuidMap into the database.
     * @param inputUuidMap
     */
    public void insert(UuidMap inputUuidMap)
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.persist(inputUuidMap);
            
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
     * Used to update an existing UuidMap within the database.
     * @param inputUuidMap
     * @throws Exception 
     */
    public void update(UuidMap inputUuidMap) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.merge(inputUuidMap);
            
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
     * Used to delete a UuidMap from within the database.
     * @param inputUuidMap 
     */
    public void delete(UuidMap inputUuidMap)
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.remove(inputUuidMap);
            
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
     * Used to generate the next valid ID for a User object.
     * @return newId
     * @throws Exception 
     */
    public int getNewUuidId() throws Exception
    {
        int newID = -1;
        
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            Query customQuery = entityManager.createQuery("SELECT MAX(u.id) FROM UuidMap u");          
            
            Object tempResult = customQuery.getSingleResult();
            
            if (tempResult == null)
            {
                newID = 0;
            }
            else
            {
                newID = (Integer) tempResult;
            }
            
            newID++;
            
            return newID;
        }
        finally
        {
            entityManager.close();
        }
    }
}
