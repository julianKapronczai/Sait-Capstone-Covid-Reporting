/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.services;

import com.sait.capstone.itsd.covidreporting.data_access_layer.CovidReportDB;
import com.sait.capstone.itsd.covidreporting.models.CovidReport;
import com.sait.capstone.itsd.covidreporting.models.User;
import java.util.Date;
import java.util.List;

/**
 * CovidReport object handler. Performs conversion from servlet logic to database logic, such as error checking, or converting user input to matching database input
 * @author Alex Hill
 */
public class CovidReportService 
{
    /**
     * fetches a list of all covid reports within the database
     * @return A list of all covid reports within the database
     * @throws Exception 
     */
    public List<CovidReport> getAll() throws Exception
    {
        CovidReportDB testCovidReport = new CovidReportDB();
        List<CovidReport> tempCovidReport = testCovidReport.getAll();
        return tempCovidReport;
    }
    
    /**
     * fetches and sorts all covid reports from the database
     * @param username
     * @param columnName specific column to sort the resultant list by
     * @return a list of all covid reports within the database sorted by the specified column
     * @throws Exception 
     */
    public List<CovidReport> getAllSorted(String columnName) throws Exception
    {
        CovidReportDB testCovidReport = new CovidReportDB();
        List<CovidReport> tempCovidReport = testCovidReport.getAllSorted(columnName);
        return tempCovidReport;
    }
    
    /**
     *  fetches a specific covid report from the database
     * @param inputCovidReportId the ID number of a specific covid report to retrieve 
     * @return the covid report associated with the supplied ID number
     * @throws Exception 
     */
    public CovidReport get(int inputCovidReportId) throws Exception
    {
        CovidReportDB testCovidReport = new CovidReportDB();
        CovidReport tempCovidReport = testCovidReport.get(inputCovidReportId);
        return tempCovidReport;
    }
    
    /**
     *  inserts a new covid report into the database
     * @param reportDate The date the report was created
     * @param oneDayDifference the one day difference in covid case numbers
     * @param sevenDayDifference the seven day difference in covid case numbers
     * @param fourteenDayDifference the fourteen day difference in covid case numbers
     * @param totalCases the total number of covid cases to date
     * @param totalActiveCases the total number of currently active covid cases
     * @param totalRecovered the total number of covid cases where the patient has recovered
     * @param totalDeaths the total number of deaths due to covid to date
     * @param positivityRate the current positivity rate of covid tests
     * @param reporter the User that created the report.
     * @throws Exception 
     */
    public void insert (Date reportDate, int oneDayDifference, int sevenDayDifference, int fourteenDayDifference
                        , int totalCases, int totalActiveCases, int totalRecovered, int totalDeaths
                        , double positivityRate, User reporter,String notes) throws Exception
    {
       
       CovidReportDB testCovidReportConnection = new CovidReportDB();
       int covidReportID = testCovidReportConnection.getNewCovidReportId();
       CovidReport newCovidReport = new CovidReport(covidReportID, reportDate,oneDayDifference,sevenDayDifference, fourteenDayDifference,totalCases,totalActiveCases,totalRecovered,totalDeaths,
                                                                   positivityRate);
       testCovidReportConnection.insert(newCovidReport);
        
    }
    
    /**
     * updates an existing covid report within the database
     * @param covidReportID the unique ID number associated with a specific covid report
     * @param reportDate The date the report was created
     * @param oneDayDifference the one day difference in covid case numbers
     * @param sevenDayDifference the seven day difference in covid case numbers
     * @param fourteenDayDifference the fourteen day difference in covid case numbers
     * @param totalCases the total number of covid cases to date
     * @param totalActiveCases the total number of currently active covid cases
     * @param totalRecovered the total number of covid cases where the patient has recovered
     * @param totalDeaths the total number of deaths due to covid to date
     * @param positivityRate the current positivity rate of covid tests
     * @param reporter the User that created the report.
     * @throws Exception 
     */
    public void update (int covidReportID, Date reportDate, int oneDayDifference, int sevenDayDifference
                        , int fourteenDayDifference, int totalCases, int totalActiveCases, int totalRecovered
                        , int totalDeaths, double positivityRate, User reporter, String notes) throws Exception
    {
        
        CovidReportDB testCovidReportConnection = new CovidReportDB();
        CovidReport tempCovidReport = this.get(covidReportID);
        
        if(tempCovidReport.getFourteenDayDifference()!= fourteenDayDifference)
        {
            tempCovidReport.setFourteenDayDifference(fourteenDayDifference);
        }
        if(tempCovidReport.getOneDayDifference()!= oneDayDifference)
        {
            tempCovidReport.setOneDayDifference(oneDayDifference);
        }
        if(tempCovidReport.getSevenDayDifference()!= sevenDayDifference)
        {
            tempCovidReport.setSevenDayDifference(sevenDayDifference);
        }
        if(tempCovidReport.getPositivityRate()!= positivityRate)
        {
            tempCovidReport.setPositivityRate(positivityRate);
        }
        if(tempCovidReport.getReportDate()!= reportDate)
        {
            tempCovidReport.setReportDate(reportDate);
        }
        if(tempCovidReport.getTotalActiveCases()!= totalActiveCases)
        {
            tempCovidReport.setTotalActiveCases(totalActiveCases);
        }
        if(tempCovidReport.getTotalCases()!= totalCases)
        {
            tempCovidReport.setTotalCases(totalCases);
        }
        if(tempCovidReport.getTotalDeaths()!= totalDeaths)
        {
            tempCovidReport.setTotalDeaths(totalDeaths);
        }
        if(tempCovidReport.getTotalRecovered()!= totalRecovered)
        {
            tempCovidReport.setTotalRecovered(totalRecovered);
        }
        testCovidReportConnection.update(tempCovidReport);
        
        
    }
    
    /**
     * deletes a specific covid report within in the database
     * @param covidReportId the ID number of the covid report to delete
     * @throws Exception 
     */
    public void delete (int covidReportID) throws Exception
    {
        CovidReportDB testCovidReportConnection = new CovidReportDB();
        CovidReport tempCovidReport = this.get(covidReportID);
        
        testCovidReportConnection.delete(tempCovidReport);
    }
    
    /**
     *  Used to insert a new CovidReport within the database
     * @param inputCovidReport
     * @throws Exception 
     */
    public void insert (CovidReport inputCovidReport) throws Exception
    {
        CovidReportDB tempCovidReportConnection = new CovidReportDB();
        tempCovidReportConnection.insert(inputCovidReport);
    }
    
    /**
     *  Returns a CovidReport as a List of String[]'s that match the supplied ID
     * @param reportID
     * @return
     * @throws Exception 
     */
    public List<String[]> getResultSetById(int reportID) throws Exception
    {
        CovidReportDB tempCovidReportConnection = new CovidReportDB();
        return tempCovidReportConnection.getResultSetById(reportID);
    }
}
