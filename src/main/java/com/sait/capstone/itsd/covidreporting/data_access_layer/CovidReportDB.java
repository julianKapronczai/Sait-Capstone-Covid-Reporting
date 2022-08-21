/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.data_access_layer;

import com.sait.capstone.itsd.covidreporting.models.CovidReport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.util.List;

/**
 * Provides database access for handling CovidReport objects
 * @author Alex Hill
 */
public class CovidReportDB 
{
    /**
     *  Used to return a list of all CovidReports that are stored within the database.
     * @return covidReportList a List of all covid reports within the database.
     * @throws Exception 
     */
    public List<CovidReport> getAll() throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        
        try
        {
            List<CovidReport> allCovidReports = entityManager.createNamedQuery("CovidReport.findAll", CovidReport.class).getResultList();
            
            return allCovidReports;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to return a list of all CovidReports within the database sorted by the specificed column name.
     * @param columnName the column and order by which to sort the resulting list
     * @return covidReportListSorted a list of all covid reports in the system sorted by the desired column.
     * @throws Exception 
     */
    public List<CovidReport> getAllSorted(String columnName) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        List<CovidReport> allCovidReportsSorted = null;
        
        try
        {
            entityTransaction.begin();

            Query customQuery = entityManager.createQuery("SELECT c FROM CovidReport c ORDER BY :columnName");
            
            customQuery.setParameter("columnName", columnName);
            
            //Query customQuery = entityManager.createNativeQuery("SELECT c FROM CovidReport c ORDER BY c. ?").setParameter(1, columnName);
            
            allCovidReportsSorted =  customQuery.getResultList();
            
            return allCovidReportsSorted;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to find and return a CovidReport based upon it's ID number.
     * @param inputCovidReportId the ID number of a covid report
     * @return covidReport a CovidReport matching the id number supplied
     * @throws Exception 
     */
    public CovidReport get (int inputCovidReportId) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        
        try
        {
            CovidReport tempCovidReport = entityManager.find(CovidReport.class, inputCovidReportId);
            return tempCovidReport;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     * Used to insert a new CovidReport into the database.
     * @param inputCovidReport a CovidReport to persist in the database
     * @throws Exception 
     */
    public void insert (CovidReport inputCovidReport) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.persist(inputCovidReport);
            
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
     * Used to update a CovidReport within the database.
     * @param inputCovidReport a CovidReport containing an update to persist within the database
     * @throws Exception 
     */
    public void update (CovidReport inputCovidReport) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.merge(inputCovidReport);
            
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
     *  Used to delete a CovidReport from the database.
     * @param inputCovidReport a CovidReport to delete from the database
     * @throws Exception 
     */
    public void delete (CovidReport inputCovidReport) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            entityManager.remove(inputCovidReport);
            
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
     * Used to generate the next valid ID for a new CovidReport
     * @return newReportId an int value of a new valid ID
     */
    public int getNewCovidReportId () throws Exception
    {
        int newID = -1;
        
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        try
        {
            entityTransaction.begin();
            
            Query customQuery = entityManager.createQuery("SELECT MAX(c.covidReportID) FROM CovidReport c");
            
            newID = (Integer) customQuery.getSingleResult();
            
            newID++;
            
            return newID;
        }
        finally
        {
            entityManager.close();
        }
    }
    
    /**
     *  Used to fetch a result from the database and return it as a direct List of String values.
     * @param reportId
     * @return
     * @throws Exception 
     */
    public List<String[]> getResultSetById (int reportId) throws Exception
    {
        EntityManager entityManager = DBUtil.getEntityFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        
        List<String[]> requestedReportAsResultSet = null;
        
        try
        {
            entityTransaction.begin();

            Query customQuery = entityManager.createQuery("SELECT c FROM CovidReport c WHERE c.covidReportID = :reportId");
            
            customQuery.setParameter("reportId", reportId);
            
            requestedReportAsResultSet = customQuery.getResultList();
            
            return requestedReportAsResultSet;
        }
        finally
        {
            entityManager.close();
        }
    }
}
