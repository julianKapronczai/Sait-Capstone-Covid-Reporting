/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.servlets;

import com.sait.capstone.itsd.covidreporting.models.CovidTestResult;
import com.sait.capstone.itsd.covidreporting.models.User;
import com.sait.capstone.itsd.covidreporting.services.CovidTestResultService;
import com.sait.capstone.itsd.covidreporting.services.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Provides server logic for the data entry page
 * @author Alex Hill
 */
public class DataEntryServlet extends HttpServlet
{
    private List<CovidTestResult> testResultList = null;
    
    /**
     * provides logic for http get requests
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String currentUser = (String) session.getAttribute("username");
        
        String columnSort = "";
        
        if(request.getParameter("columnSort") != null)
        {
            columnSort = request.getParameter("columnSort");
        }
        
        String sortOrder = null;
        
        if (session.getAttribute("sortOrder") != null)
        {
            String previousOrder = (String) session.getAttribute("sortOrder");
            
            if (previousOrder.equals(" DESC"))
            {
                sortOrder = " ASC";
            }
            else if (previousOrder.equals(" ASC"))
            {
                sortOrder = " DESC";
            }
        }
        else
        {
            sortOrder = " DESC";
        }
        
        session.setAttribute("sortOrder", sortOrder);
        
        try
        {
            switch (columnSort)
            {
                case "Result Date":                     this.testResultList = new CovidTestResultService().getAllSorted(currentUser, "ResultDate" + sortOrder);
                                                        break;
                case "Submission Date":                 this.testResultList = new CovidTestResultService().getAllSorted(currentUser, "SubmissionDate" + sortOrder);
                                                        break;
                case "Number of Tests Taken":           this.testResultList = new CovidTestResultService().getAllSorted(currentUser, "NumberTestsTaken" + sortOrder);
                                                        break;
                case "Number of Positive Tests":        this.testResultList = new CovidTestResultService().getAllSorted(currentUser, "NumberTestsPositive" + sortOrder);
                                                        break;
                case "Number of Inconclusive Tests":    this.testResultList = new CovidTestResultService().getAllSorted(currentUser, "NunmberTestsInconclusive" + sortOrder);
                                                        break;
                case "Positivity Rate":                 this.testResultList = new CovidTestResultService().getAllSorted(currentUser, "PositivityRate" + sortOrder);
                                                        break;
                case "Notes":                           this.testResultList = new CovidTestResultService().getAllSorted(currentUser, "Notes" + sortOrder);
                                                        break;
                default:                                this.testResultList = new CovidTestResultService().getAllSorted(currentUser, "CovidTestResultID" + " ASC");
                                                        break;
            }

            session.setAttribute("testResultList", this.testResultList);
        }
        catch (Exception ex)
        {
            Logger.getLogger(DataEntryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/DataEntry.jsp").forward(request, response);
        return;
    }
    
    /**
     * provides logic for http post requests
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String currentUser = (String) session.getAttribute("username");
        
        String action = request.getParameter("action");
        
        boolean changesMade = false;
        Object[] returnArray = null;
        
        switch (action)
        {
            case "addResult":
                                        returnArray = this.addResult(request, response, session);
                                        
                                        request = (HttpServletRequest) returnArray[0];
                                        response = (HttpServletResponse) returnArray[1];
                                        session =  (HttpSession) returnArray[2];
                                        changesMade = (boolean) returnArray[3];
                                        break;
            case "updateResult":
                                        returnArray = this.updateResult(request, response, session);
                                        
                                        request = (HttpServletRequest) returnArray[0];
                                        response = (HttpServletResponse) returnArray[1];
                                        session =  (HttpSession) returnArray[2];
                                        changesMade = (boolean) returnArray[3];
                                        break;
            case "edit":
                                        returnArray = this.editResult(request, response, session);
                                        
                                        request = (HttpServletRequest) returnArray[0];
                                        response = (HttpServletResponse) returnArray[1];
                                        session =  (HttpSession) returnArray[2];
                                        changesMade = (boolean) returnArray[3];
                                        break;
            case "delete":
                                        returnArray = this.deleteResult(request, response, session);
                                        
                                        request = (HttpServletRequest) returnArray[0];
                                        response = (HttpServletResponse) returnArray[1];
                                        session =  (HttpSession) returnArray[2];
                                        changesMade = (boolean) returnArray[3];
                                        break;
            case "displayAddForm":
                                        request.setAttribute("showAddForm", true);
                                        break;
        }
        
        if (changesMade)
        {
            try
            {
                this.testResultList = new CovidTestResultService().getAllSorted(currentUser, "CovidTestResultID" + " ASC");

                session.setAttribute("testResultList", this.testResultList);
                
                getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/DataEntry.jsp").forward(request, response);
                return;
            }
            catch (Exception ex)
            {
                Logger.getLogger(DataEntryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/DataEntry.jsp").forward(request, response);
            return;
        }
    }
    
    /**
     * provides logic for adding a new covid test result into the system
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private Object[] addResult(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        String sessionUsername = (String) session.getAttribute("username");
        
        boolean changesMade = false;
        
        if (request.getParameter("closeForm") != null)
        {
            // DO NOTHING IF THE USER RESET THE FORM AND JUST RELOAD THE PAGE.
            // USE THIS TO CLOSE THE FORM WHEN YOU IMPLEMENT THE x BUTTON
            request.setAttribute("showAddForm", false);
            
            Object[] returnArray = {request, response, session, changesMade};
        
            return returnArray;
        }
        
        String inputResultDate = request.getParameter("newResultDate");
        String inputNumberTestsTaken = request.getParameter("newTestsTaken");
        String inputNumberTestsPositive = request.getParameter("newTestsPositive");
        String inputNumberTestsInconclusive = request.getParameter("newTestsInconclusive");
        String inputNotes = request.getParameter("newNotes");
        
        ArrayList<String> errorChecking = new ArrayList<String>();
        
        errorChecking.add(inputResultDate);
        errorChecking.add(inputNumberTestsTaken);
        errorChecking.add(inputNumberTestsPositive);
        errorChecking.add(inputNumberTestsInconclusive);
        
        for(String input: errorChecking)
        {
            if (input.equals("") || input == null)
            {
                request.setAttribute("server_message", "All fields, except notes, must be filled out in order to add a test result to the database.<br>Please fill out all the add test result fields.");

                request.setAttribute("showAddForm", true);

                getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/DataEntry.jsp").forward(request, response);
            }
        }
        
        Date resultDate = null;
        
        try 
        {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            resultDate = dateFormatter.parse(inputResultDate);
        } 
        catch (ParseException ex) 
        {
            Logger.getLogger(DataEntryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int numberTestsTaken = Integer.parseInt(inputNumberTestsTaken);
        int numberTestsPositive = Integer.parseInt(inputNumberTestsPositive);
        int numberTestsInconclusive = Integer.parseInt(inputNumberTestsInconclusive);
        double positivityRate = (numberTestsPositive * 1.0) / ((numberTestsTaken - numberTestsInconclusive) * 1.0);
        
        // get the current date to use for SubmissionDate
        long currentDateInMillis = System.currentTimeMillis(); 
        Date submissionDate = new Date(currentDateInMillis);
        
        User currentUser = null;
        try 
        {
            // get userID of current user for ReporterID field
            currentUser = new UserService().getByUsername(sessionUsername);
            
            new CovidTestResultService().insert(resultDate, submissionDate, numberTestsTaken, numberTestsPositive, positivityRate, numberTestsInconclusive, currentUser, inputNotes);
            
            changesMade = true;
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(DataEntryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        Object[] returnArray = {request, response, session, changesMade};
        return returnArray;
    }
    
    /**
     * provides logic for updating an existing covid test result within the system
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private Object[] updateResult(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        boolean changesMade = false;
        
        if (session.getAttribute("selectedTestResult") == null)
        {
            request.setAttribute("server_message", "Use the edit button within the Covid Test Result pane to update a test result.");
        }
        else if (request.getParameter("closeForm") != null)
        {
            // DO NOTHING IF THE USER RESET THE FORM AND JUST RELOAD THE PAGE.
            // USE THIS TO CLOSE THE FORM WHEN YOU IMPLEMENT THE x BUTTON

            request.setAttribute("showEditForm", false);
            
            Object[] returnArray = {request, response, session, changesMade};
        
            return returnArray;
        }
        else
        {
            int selectedTestResultId = Integer.parseInt(request.getParameter("selectedTestResultID"));
            
            try
            {
                CovidTestResult selectedTestResult = new CovidTestResultService().get(selectedTestResultId);
                
                String rawUpdatedResultDate = request.getParameter("editResultDate");
                String rawUpdatedNumberTaken = request.getParameter("editNumberTaken");
                String rawUpdatedNumberPositive = request.getParameter("editNumberPositive");
                String rawUpdatedNumberInconclusive = request.getParameter("editNumberInconclusive");
                String updatedNotes = request.getParameter("editNotes");
                
                Date updatedResultDate = null;
                int updatedNumberTaken = -1;
                int updatedNumberPositive = -1;
                int updatedNumberInconclusive = -1;
                double updatedPositivityRate = -1;
                
                boolean recalculatePositivityRate = false;
                
                if (rawUpdatedResultDate.equals("") || rawUpdatedResultDate == null)
                {
                    updatedResultDate = selectedTestResult.getResultDate();
                }
                else
                {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                    
                    updatedResultDate = dateFormatter.parse(rawUpdatedResultDate);
                }
                
                if (rawUpdatedNumberTaken.equals("") || rawUpdatedNumberTaken == null)
                {
                    updatedNumberTaken = selectedTestResult.getNumberTestsTaken();
                }
                else
                {
                    updatedNumberTaken = Integer.parseInt(rawUpdatedNumberTaken);
                    recalculatePositivityRate = true;
                    
                }
                
                if (rawUpdatedNumberPositive.equals("") || rawUpdatedNumberPositive == null)
                {
                    updatedNumberPositive = selectedTestResult.getNumberTestsPositive();
                }
                else
                {
                    updatedNumberPositive = Integer.parseInt(rawUpdatedNumberPositive);
                    recalculatePositivityRate = true;
                }
                
                if (rawUpdatedNumberInconclusive.equals("") || rawUpdatedNumberInconclusive == null)
                {
                    updatedNumberInconclusive = selectedTestResult.getNumberTestsInconclusive();
                }
                else
                {
                    updatedNumberInconclusive = Integer.parseInt(rawUpdatedNumberInconclusive);
                    
                    recalculatePositivityRate = true;
                }
                
                if (recalculatePositivityRate == true)
                {
                    updatedPositivityRate = (updatedNumberPositive * 1.0) / ((updatedNumberTaken - updatedNumberInconclusive) * 1.0);
                }
                else
                {
                    updatedPositivityRate = selectedTestResult.getPositivityRate();
                }
                
                if (updatedNotes.equals("") || updatedNotes == null)
                {
                    updatedNotes = selectedTestResult.getNotes();
                }

                new CovidTestResultService().update(selectedTestResultId, updatedResultDate, updatedNumberTaken, updatedNumberPositive, updatedNumberInconclusive, updatedPositivityRate, updatedNotes);

                request.setAttribute("server_message", "Covid Test Result was successfully updated in the database.");

                changesMade = true;
            }
            catch (Exception ex)
            {
                Logger.getLogger(UserManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        session.removeAttribute("selectedTestResult");
        
        Object[] returnArray = {request, response, session, changesMade};
        return returnArray;
    }
    
    /**
     * provides logic for editing a covid test result on the front-end
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private Object[] editResult(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        boolean changesMade = false;
        
        int selectedTestResultId = Integer.parseInt(request.getParameter("selectTestResult"));
        try
        {
            CovidTestResult selectedTestResult = new CovidTestResultService().get(selectedTestResultId);

            session.setAttribute("selectedTestResult", selectedTestResult);
            request.setAttribute("showEditForm", true);
            request.setAttribute("selectedTestResultID", selectedTestResultId);
            request.setAttribute("editResultDate", selectedTestResult.getResultDate());
            request.setAttribute("editNumberTaken", selectedTestResult.getNumberTestsTaken());
            request.setAttribute("editNumberPositive", selectedTestResult.getNumberTestsPositive());
            request.setAttribute("editNumberInconclusive", selectedTestResult.getNumberTestsInconclusive());
            request.setAttribute("editNotes", selectedTestResult.getNotes());
        }
        catch (Exception ex)
        {
            Logger.getLogger(UserManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Object[] returnArray = {request, response, session, changesMade};
        return returnArray;
    }
    
    /**
     * provides logic for deleting a covid test result from the system
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private Object[] deleteResult(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        boolean changesMade = false;
        
        int testResultIdToDelete = Integer.parseInt(request.getParameter("selectTestResult"));
        
        CovidTestResult testResultToDelete = null;
        
        try 
        {
            testResultToDelete = new CovidTestResultService().get(testResultIdToDelete);
            if(!testResultToDelete.getReporterID().getUsername().equals(session.getAttribute("username").toString().toLowerCase()))
            {
                request.setAttribute("server_message", "Stop trying to delete things you aren't responsible for.");
            }
            else
            {
                new CovidTestResultService().delete(testResultIdToDelete);

                request.setAttribute("server_message", "Covid test result successfully delete from the database.");

                changesMade = true;    
            }
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(DataEntryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        session.removeAttribute("testResultIdToDelete");
        
        Object[] returnArray = {request, response, session, changesMade};
        return returnArray;
    }
}
