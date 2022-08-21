/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.data_access_layer;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import com.sait.capstone.itsd.covidreporting.models.Role;
import com.sait.capstone.itsd.covidreporting.models.User;

/**
 * Provides database access for handling User objects
 * @author Alex Hill
 */
public class UserDB
{
    /**
     * Used to return a list of all Users within the database.
     * @return allUserList
     * @throws Exception 
     */
    public List<User> getAll() throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        
        try
        {
           List<User> allUserList = entityManager.createNamedQuery("User.findAll", User.class).getResultList();
           return allUserList;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to return a list of all Users within the database sorted by the specified column.
     * @param columnName
     * @return allUserListSorted
     * @throws Exception 
     */
    public List<User> getAllSorted(String columnName) throws Exception
    {        
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        List<User> allUserListSorted = null;
        
        try
        {
            entityTransaction.begin();
            
            Query customQuery = entityManager.createQuery("SELECT u FROM User u ORDER BY u." + columnName);
            
            allUserListSorted =  customQuery.getResultList();
            
            return allUserListSorted;
        }
        finally
        {
            entityManager.close();
        }
    }

    /**
     * Used to find the User that has the specified username.
     * @param inputUsername
     * @return tempUser
     * @throws Exception 
     */
    public User getByUsername(String inputUsername) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        User tempUser = null;
        try
        {
            tempUser = entityManager.createNamedQuery("User.findByUsername", User.class)
                                            .setParameter("username", inputUsername.toLowerCase())
                                            .getSingleResult();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            entityManager.close();
        }

        return tempUser;
    }
    
    /**
     * Used to find the User that has the specified e-mail address.
     * @param inputEmail
     * @return user
     * @throws Exception 
     */
    public User getByEmail(String inputEmail) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        User user = null;
        try
        {
            user = entityManager.createNamedQuery("User.findByEmail", User.class)
                                            .setParameter("email", inputEmail)
                                            .getSingleResult();
        } 
        catch (Exception ex)
        {
            //ex.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        
        return user;
    }
    
    /**
     * Used to find the User that has the specified user id number.
     * @param inputUserId
     * @return user
     * @throws Exception 
     */
    public User getById(int inputUserId) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        User user = null;
        try
        {
            user = entityManager.createNamedQuery("User.findByUserID", User.class)
                                            .setParameter("userID", inputUserId)
                                            .getSingleResult();
        } 
        catch (Exception ex)
        {
            //ex.printStackTrace();
        }
        finally {
            entityManager.close();
        }
        
        return user;
    }
    
    /**
     * Used to insert a new User into the database.
     * @param inputUser
     * @throws Exception 
     */
    public void insert(User inputUser) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            //System.out.println(inputUser);
            Role userRole = inputUser.getRole();
            
            //make sure the role object now contains the new user
            userRole.getUserList().add(inputUser);
            
            entityTransaction.begin();
            
            entityManager.persist(inputUser);
            
            //merge the role (aka update the role so that it contains the new user)
            entityManager.merge(userRole);
            
            entityTransaction.commit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            entityTransaction.rollback();
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to update an existing User within the database.
     * @param originalUsername
     * @param inputUser
     * @throws Exception 
     */
    public void update(String originalUsername, User inputUser) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            User originalUserFromDB = entityManager.merge(this.getByUsername(originalUsername));
            
            Role originalUserRole = originalUserFromDB.getRole();
            Role inputUserRole = inputUser.getRole();
            
            //make sure old role no longer contains the user
            originalUserRole.getUserList().remove(originalUserFromDB);
            
            if (!originalUserRole.equals(inputUserRole))
            {
                //add user to new role
                inputUserRole.getUserList().add(inputUser);
            }
            
            entityManager.remove(originalUserFromDB);
            
            if (originalUserFromDB.getUsername().equals(inputUser.getUsername()))
            {
                //must remove 'old' user and commit before you can persist 'new' user.
                //Only needed if usernames aren't changing
                //really shouldn't be needed as primary key should be an autogenerated ID column to avoid this sort of issue.
                entityTransaction.commit();
                entityTransaction.begin();
            }

            entityManager.persist(inputUser);
            
            //merge the role (aka update the role so that it contains the new user)
            entityManager.merge(originalUserRole);
            
            entityManager.merge(inputUserRole);
            
            entityTransaction.commit();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
            entityTransaction.rollback();
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to update the active status of an existing User within the database.
     * @param inputUser
     * @throws Exception 
     */
    public void updateActiveStatus(User inputUser) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            User confirmedUser = entityManager.merge(this.getByUsername(inputUser.getUsername()));
            
            confirmedUser.setActive(inputUser.getActive());
            
            entityManager.merge(confirmedUser);
            
            entityTransaction.commit();
        }
        catch (Exception ex)
        {
            System.out.println(ex);
            entityTransaction.rollback();
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to update the password for the specified User within the database.
     * @param inputUser
     * @throws Exception 
     */
    public void updatePassword(User inputUser) throws Exception 
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            User originalUserFromDB = this.getByUsername(inputUser.getUsername());
            
            originalUserFromDB.setPassword(inputUser.getPassword());
            
            entityManager.merge(originalUserFromDB);
            
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
     * USed to delete an existing User from within the database.
     * @param inputUser
     * @throws Exception 
     */
    public void delete(User inputUser) throws Exception 
    {        
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            User confirmedUser = entityManager.merge(this.getByUsername(inputUser.getUsername()));
            
            Role userRole = confirmedUser.getRole();
            
            //make sure the role object no longer contains the new user
            userRole.getUserList().remove(confirmedUser);
            
            entityManager.remove(confirmedUser);
            
            //merge the role (aka update the role so that it doesn't contain the new user)
            entityManager.merge(userRole);
            
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
    public int getNewUserId() throws Exception
    {
        int newID = -1;
        
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            Query customQuery = entityManager.createQuery("SELECT MAX(u.userID) FROM User u");       
            
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
