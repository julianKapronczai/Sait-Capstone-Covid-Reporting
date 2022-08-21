/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.services;

import com.sait.capstone.itsd.covidreporting.data_access_layer.CovidTestResultDB;
import com.sait.capstone.itsd.covidreporting.models.CovidTestResult;
import com.sait.capstone.itsd.covidreporting.models.User;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CovidTestResult object handler. Performs conversion from servlet logic to database logic, such as error checking, or converting user input to matching database input
 * @author Alex Hill
 */
public class CovidTestResultService 
{
    /**
     * fetches a list of all covid test results within the database
     * @return a list of all covid test results within the database
     * @throws Exception 
     */
    public List<CovidTestResult> getAll() throws Exception
    {
        CovidTestResultDB testResultConnection = new CovidTestResultDB();
        List<CovidTestResult> tempCovidTestResult = testResultConnection.getAll();
        return tempCovidTestResult;
    }
    
    /**
     * fetches and sorts all covid test results from the database
     * @param username
     * @param columnName specific column to sort the resultant list by
     * @return a list of all covid test results within the database sorted by the specified column
     * @throws Exception 
     */
    public List<CovidTestResult> getAllSorted (String username, String columnName) throws Exception
    {
        UserService testUserService = new UserService();
        User tempUser = testUserService.getByUsername(username);
        
        CovidTestResultDB testResultConnection = new CovidTestResultDB();
        
        List<CovidTestResult> tempCovidTestResult = null;
        
        if (new AccountService().userIsAdmin(username))
        {
            tempCovidTestResult = testResultConnection.getAll();
        }
        else
        {
            tempCovidTestResult = testResultConnection.getAllSortedForUser(tempUser, columnName);
        }

        return tempCovidTestResult;
    }
    
    /**
     * fetches a specific covid test result from the database
     * @param covidTestResultId the specific ID number of the covid test result to retrieve from the database
     * @return the associated covid test result from the database
     * @throws Exception 
     */
    public CovidTestResult get (int covidTestResultId) throws Exception
    {
        CovidTestResultDB testResultConnection = new CovidTestResultDB();
        CovidTestResult tempCovidTestResult = testResultConnection.get(covidTestResultId);
        return tempCovidTestResult;
    }
    
    /**
     * inserts a new covid test result into the database
     * @param resultDate date the test was taken
     * @param submissionDate date the test was submitted to the system
     * @param numberTestsTaken the total number of tests taken
     * @param numberTestsPositive the total number of positive tests
     * @param positivityRate the positivity rate of the tests
     * @param numberTestsInconclusive
     * @param reporter the User that submitted the test result to the system
     * @throws Exception 
     */
    public void insert (Date resultDate, Date submissionDate, int numberTestsTaken
                        , int numberTestsPositive, double positivityRate, int numberTestsInconclusive, User reporter, String notes) throws Exception
    {
        CovidTestResultDB testResultConnection = new CovidTestResultDB();
        
        int newTempCovidTestResult = testResultConnection.getNewCovidTestResultId();
        
        CovidTestResult newCovidTestResult = new CovidTestResult(newTempCovidTestResult, resultDate, submissionDate, numberTestsTaken, numberTestsPositive, numberTestsInconclusive, positivityRate);
        
        newCovidTestResult.setNotes(notes);
        
        newCovidTestResult.setReporterID(reporter);

        testResultConnection.insert(newCovidTestResult);
                        
    }
    
    /**
     * updates an existing covid test result within the database
     * @param covidTestResultId the unique ID number of a test result to update
     * @param resultDate date the test was taken
     * @param submissionDate date the test was submitted to the system
     * @param numberTestsTaken the total number of tests taken
     * @param numberTestsPositive the total number of positive tests
     * @param positivityRate the positivity rate of the tests
     * @param reporter the User that submitted the test result to the system
     * @throws Exception 
     */
    public void update (int covidTestResultId, Date resultDate, Date submissionDate
                        , int numberTaken, int numberPositive, double positivityRate, int numberInconclusive,
                         User reporter, String notes) throws Exception
    {
        int testResultID = 0;
        
        CovidTestResultDB testResultConnection = new CovidTestResultDB();
        CovidTestResult tempCovidTestResult = this.get(testResultID);
        CovidTestResult tempResultID = this.get(testResultID);
        User tempUser = new User();
        tempUser = tempResultID.getReporterID();
        User tempReporterID = new User();
        tempReporterID = tempResultID.getReporterID();
        Date tempSubmissionDate = tempResultID.getSubmissionDate();
        
        if(!tempCovidTestResult.getNotes().equals(notes))
        {
        tempCovidTestResult.setNotes(notes);
        }
        if(!tempCovidTestResult.getNotes().equals(notes))
        {
            tempCovidTestResult.setNotes(notes);
        }
        if(tempCovidTestResult.getNumberTestsInconclusive()!= numberInconclusive)
        {
            tempCovidTestResult.setNumberTestsInconclusive(numberInconclusive);
        }
        if(tempCovidTestResult.getNumberTestsPositive()!= numberPositive)
        {
            tempCovidTestResult.setNumberTestsPositive(numberPositive);
        }
        if(tempCovidTestResult.getNumberTestsTaken()!= numberTaken)
        {
            tempCovidTestResult.setNumberTestsTaken(numberTaken);
        }
        if(tempCovidTestResult.getPositivityRate()!= positivityRate)
        {
            tempCovidTestResult.setPositivityRate(positivityRate);
        }
        if(tempCovidTestResult.getReporterID()!= tempReporterID)
        {
            tempCovidTestResult.setReporterID(tempReporterID);
        }
        if(tempCovidTestResult.getResultDate()!= resultDate)
        {
            tempCovidTestResult.setResultDate(resultDate);
        }
        if(tempCovidTestResult.getSubmissionDate()!= tempSubmissionDate)
        {
            tempCovidTestResult.setSubmissionDate(tempSubmissionDate);
        }
        testResultConnection.update(tempCovidTestResult);
    }
    /**
     * deletes a specific covid test result in the database
     * @param covidTestResultId the unique ID number of a test result to update
     * @throws Exception 
     */
    public void delete (int covidTestResultId) throws Exception
    {
        CovidTestResultDB testResultConnection = new CovidTestResultDB();
        CovidTestResult tempCovidTestResult = testResultConnection.get(covidTestResultId);
        
        testResultConnection.delete(tempCovidTestResult);
    }
    
    /**
     * fetches a list of all covid test results that occured after a specific date
     * @param minDate the earliest date of test results to retrieve
     * @param columnName
     * @return a list of all covid test results that occured after the specified date
     * @throws Exception 
     */
    public List<CovidTestResult> getAllAfterDate(Date minDate, String columnName) throws Exception
    {
        CovidTestResultDB testResultConnection = new CovidTestResultDB();
        List<CovidTestResult> covidTestResultsAfterDate = testResultConnection.getAllAfterDate(minDate, columnName);
      return covidTestResultsAfterDate;  
    }
    
    /**
     * Used to update a specific CovidTestResult within the database
     * @param testResultID ID of the test result
     * @param resultDate Date the test was taken
     * @param numberTaken number of tests taken
     * @param numberPositive number of tests that were positive
     * @param numberInconclusive number of tests that were inconclusive
     * @param positivityRate the overall positivity rate for the CovidTestResult
     * @param notes any notes attached to the CovidTestResult
     */
    public void update (int testResultID, Date resultDate, int numberTaken, int numberPositive, int numberInconclusive, double positivityRate, String notes){
        
        try {
        CovidTestResultDB testResultConnection = new CovidTestResultDB();
        CovidTestResult tempCovidTestResult = this.get(testResultID);
                  
        CovidTestResult tempResultID = this.get(testResultID);
        User tempReporterID = new User();
        tempReporterID = tempResultID.getReporterID();
        Date tempSubmissionDate = tempResultID.getSubmissionDate();
        
        if(!tempCovidTestResult.getNotes().equals(notes))
        {
            tempCovidTestResult.setNotes(notes);
        }
        if(tempCovidTestResult.getNumberTestsInconclusive()!= numberInconclusive)
        {
            tempCovidTestResult.setNumberTestsInconclusive(numberInconclusive);
        }
        if(tempCovidTestResult.getNumberTestsPositive()!= numberPositive)
        {
            tempCovidTestResult.setNumberTestsPositive(numberPositive);
        }
        if(tempCovidTestResult.getNumberTestsTaken()!= numberTaken)
        {
            tempCovidTestResult.setNumberTestsTaken(numberTaken);
        }
        if(tempCovidTestResult.getPositivityRate()!= positivityRate)
        {
            tempCovidTestResult.setPositivityRate(positivityRate);
        }
        if(tempCovidTestResult.getReporterID()!= tempReporterID)
        {
            tempCovidTestResult.setReporterID(tempReporterID);
        }
        if(tempCovidTestResult.getResultDate()!= resultDate)
        {
            tempCovidTestResult.setResultDate(resultDate);
        }
        if(tempCovidTestResult.getSubmissionDate()!= tempSubmissionDate)
        {
            tempCovidTestResult.setSubmissionDate(tempSubmissionDate);
        }
        testResultConnection.update(tempCovidTestResult);
        
        } catch (Exception ex) {
            Logger.getLogger(CovidTestResultService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
