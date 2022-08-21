/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.services;

import com.sait.capstone.itsd.covidreporting.data_access_layer.OrganisationDB;
import com.sait.capstone.itsd.covidreporting.models.Organisation;
import java.util.List;

/**
 * Organisation object handler. Performs conversion from servlet logic to database logic, such as error checking, or converting user input to matching database input
 * @author Alex Hill
 */
public class OrganisationService 
{   
    /**
     * fetches a specific organisation from the database
     * @param inputOrganisationId the unique id number of an organisation
     * @return the organisation associated with the specified Id number
     * @throws Exception 
     */
    public Organisation get (int inputOrganisationId)  throws Exception
    {
        OrganisationDB organisationConnection = new OrganisationDB();
        Organisation tempUser = organisationConnection.get(inputOrganisationId);
        return tempUser;
    }
     
    /**
     * fetches a list of all organisations from the database
     * @return a list of all organisations within the database
     * @throws Exception 
     */
    public List<Organisation> getAll() throws Exception
    {
        OrganisationDB organisationConnection = new OrganisationDB();
        List<Organisation> tempOrganisationList = organisationConnection.getAll();
        return tempOrganisationList;
    }
    
    /**
     * fetches and sorts a list of organisations from the database
     * @param columnName the specific column to sort the resultant list by
     * @return a list of all organisations within the database sorted by the specified column
     * @throws Exception 
     */
    public List<Organisation> getAllSorted(String columnName) throws Exception
    {
        OrganisationDB organisationConnection = new OrganisationDB();
        List<Organisation> tempOrganisationList = organisationConnection.getAllSorted(columnName);
        return tempOrganisationList;
    }
    
    /**
     * inserts a new organisation into the database
     * @param newOrganisationName the name of a new organisation to add to the database.
     * @throws Exception 
     */
    public void insert (String newOrganisationName) throws Exception
    {
        OrganisationDB organisationConnection = new OrganisationDB();
        int tempNewId = organisationConnection.getNewOrganisationId();
        Organisation newOrganisation = new Organisation(tempNewId, newOrganisationName);
        organisationConnection.insert(newOrganisation);       
    }
}
