/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.data_access_layer;

import com.sait.capstone.itsd.covidreporting.models.CovidTestResult;
import com.sait.capstone.itsd.covidreporting.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;

/**
 * Provides database access for handling CovidTestResult objects
 * @author Alex Hill
 */
public class CovidTestResultDB 
{
    /**
     * Used to return a list of all CovidTestResults within the database.
     * @return covidTestResultList
     * @throws Exception 
     */
    public List<CovidTestResult> getAll() throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        
        try
        {
            List<CovidTestResult> covidTestResult = entityManager.createNamedQuery("CovidTestResult.findAll").getResultList();
            
            return covidTestResult;
        }
        finally
        {
            entityManager.close();
        }
        
    }
    
    /**
     * Used to return a list of all CovidTestResults within the database sorted by the specified column name.
     * @param username
     * @param columnName
     * @return covidTestResultListSorted
     * @throws Exception 
     */
    public List<CovidTestResult> getAllSortedForUser(User inputUser, String columnName) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        List<CovidTestResult> covidTestResultSorted = null;
        
       
        try
        {
            entityTransaction.begin();
            
            Query customQuery = entityManager.createQuery("SELECT c FROM CovidTestResult c WHERE c.reporterID = :inputUser ORDER BY :columnName ");
            customQuery.setParameter("inputUser", inputUser);
            customQuery.setParameter("columnName", columnName);
            
            System.out.println(customQuery);
            
            covidTestResultSorted = customQuery.getResultList();
            
            return covidTestResultSorted;
            
        } 
        finally
        {
            entityManager.close();
        }
        
    }
    
    /**
     * Used to return a list of all CovidTestResults that were created after the specified minimum date.
     * @param minDate
     * @return covidTestResultListAfterDate
     * @throws Exception 
     */
    public List<CovidTestResult> getAllAfterDate(Date minDate, String columnName) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        List<CovidTestResult> getAllAfterDate = null;
         try
         {
             entityTransaction.begin();
             
             Query customQuery = entityManager.createQuery("SELECT c FROM CovidTestResult c WHERE c.resultDate > :minDate ORDER BY :columnName ");
             
             customQuery.setParameter("minDate", minDate);
             customQuery.setParameter("columnName", columnName);
             
             getAllAfterDate = customQuery.getResultList();
             
             return getAllAfterDate;
         }
         finally
         {
             entityManager.close();
         }
        
    }
    
    /**
     * Used to find and return a specific CovidTestResult using its ID number.
     * @param inputCovidTestResultId
     * @return covidTestResult
     * @throws Exception 
     */
    public CovidTestResult get (int inputCovidTestResultId) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        try
        {
            
            CovidTestResult tempCovidTestResult = entityManager.find(CovidTestResult.class, inputCovidTestResultId);
            return tempCovidTestResult;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to insert a new CovidTestResult into the database.
     * @param inputCovidTestResult
     * @throws Exception 
     */
    public void insert (CovidTestResult inputCovidTestResult) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.persist(inputCovidTestResult);
            
            entityTransaction.commit();
        }
        catch (Exception ex)
        {
            //ex.printStackTrace();
            entityTransaction.rollback();
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     *  Used to update an existing CovidTestResult within the database.
     * @param inputCovidTestResult
     * @throws Exception 
     */
    public void update (CovidTestResult inputCovidTestResult) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            
            entityTransaction.begin();
            
            entityManager.merge(inputCovidTestResult);
            
            entityTransaction.commit();
            
        }
        catch(Exception ex)
        {
            entityTransaction.rollback();
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to delete a CovidTestResult from the database.
     * @param inputCovidTestResult
     * @throws Exception 
     */
    public void delete (CovidTestResult inputCovidTestResult) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            CovidTestResult confirmedTestResult = entityManager.merge(this.get(inputCovidTestResult.getCovidTestResultID()));
            
            User reporter = confirmedTestResult.getReporterID();
            
            reporter.getCovidTestResultList().remove(confirmedTestResult);
            
            entityManager.remove(confirmedTestResult);
            
            entityTransaction.commit();
        }
        catch(Exception ex)
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
     * Used to generate the next valid ID number for a new CovidTestResult.
     * @return newCovidTestResultId
     * @throws Exception 
     */
    public int getNewCovidTestResultId() throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        int newId = -1;
        try
        {
            entityTransaction.begin();
            
            Query customQuery = entityManager.createQuery("SELECT MAX(c.covidTestResultID) FROM CovidTestResult c");
            
            newId = (Integer) customQuery.getSingleResult();
            
            newId++;
            return newId;
            
        }
        finally
        {
            entityManager.close();
        }
        
    }
}
