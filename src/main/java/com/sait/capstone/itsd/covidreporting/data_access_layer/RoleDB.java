/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.data_access_layer;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.sait.capstone.itsd.covidreporting.models.Role;
import jakarta.persistence.Query;

/**
 * Provides database access for handling Role objects
 * @author Alex Hill
 */
public class RoleDB
{
    /**
     * Used to return a list of all Roles within the database.
     * @return allRoleList
     * @throws Exception 
     */
    public List<Role> getAll() throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        
        try
        {
           List<Role> allRoleList = entityManager.createNamedQuery("Role.findAll", Role.class).getResultList();
           return allRoleList;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to find and return a specific Role using its unique ID number.
     * @param inputRoleID
     * @return tempRole
     * @throws Exception 
     */
    public Role get(int inputRoleID) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        
        try
        {
            Role tempRole = entityManager.find(Role.class, inputRoleID);
            return tempRole;
        }
        finally
        {
            entityManager.close();
        }
    }

    /**
     * Used to insert a new Role into the database.
     * @param inputRole
     * @throws Exception 
     */
    public void insert(Role inputRole) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.persist(inputRole);
            
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
     * Used to update an existing Role within the database.
     * @param inputRole
     * @throws Exception 
     */
    public void update(Role inputRole) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.merge(inputRole);
            
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
     * Used to delete a Role from the database.
     * @param inputRole
     * @throws Exception 
     */
    public void delete(Role inputRole) throws Exception 
    {        
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.remove(inputRole);
            
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
     * Used to return the Role object that has the same name as the specified input.
     * @param lookupString
     * @return tempRole
     * @throws Exception 
     */
    public Role roleIDLookup(String lookupString) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        
        try
        {
            Role tempRole = entityManager.createNamedQuery("Role.findByRoleName", Role.class)
                                            .setParameter("roleName", lookupString)
                                            .getSingleResult();
            
            return tempRole;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to generate the next valid ID for a Role object.
     * @return newRoleId
     * @throws Exception 
     */
    public int getNewRoleId() throws Exception
    {
        int newID = -1;
        
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            Query customQuery = entityManager.createQuery("SELECT MAX(r.RoleId) FROM Role r");
            
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
