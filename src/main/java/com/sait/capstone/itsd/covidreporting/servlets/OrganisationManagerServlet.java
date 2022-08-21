/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.servlets;

import com.sait.capstone.itsd.covidreporting.models.Organisation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.sait.capstone.itsd.covidreporting.services.OrganisationService;

/**
 * Provides server logic for the user management page
 * @author Alex Hill
 */
public class OrganisationManagerServlet extends HttpServlet
{
    
    private List<Organisation> organisationList = null;
    
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
                case "Organisation Name":   this.organisationList = new OrganisationService().getAllSorted("organisationName" + sortOrder);
                                            break;
                default:                    this.organisationList = new OrganisationService().getAllSorted("organisationName" + " ASC");
                                            break;
            }

            session.setAttribute("organisationList", this.organisationList);
        }
        catch (Exception ex)
        {
            Logger.getLogger(OrganisationManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/organisationManager.jsp").forward(request, response);
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
        try
        {
            this.organisationList = new OrganisationService().getAllSorted("organisationName" + " ASC");

            session.setAttribute("organisationList", this.organisationList);
        }
        catch (Exception ex)
        {
            Logger.getLogger(OrganisationManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        String action = request.getParameter("action");
        
        boolean changesMade = false;
        Object[] returnArray = null;
        
        switch (action)
        {
            case "addOrganisation":
                                        returnArray = this.addOrganisation(request, response, session);
                                        
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
                this.organisationList = new OrganisationService().getAllSorted("organisationName" + " ASC");

            session.setAttribute("organisationList", this.organisationList);
                
                getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/organisationManager.jsp").forward(request, response);
                return;
            }
            catch (Exception ex)
            {
                Logger.getLogger(OrganisationManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else
        {
            getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/organisationManager.jsp").forward(request, response);
            return;
        }
    }
    
    /* * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * *              Organisation Admin               * * 
     * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     * provides logic for adding a new organisation to the system
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private Object[] addOrganisation(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        boolean changesMade = false;
        
        if (request.getParameter("closeForm") != null)
        {
            // DO NOTHING IF THE USER RESET THE FORM AND JUST RELOAD THE PAGE.
            // USE THIS TO CLOSE THE FORM WHEN YOU IMPLEMENT THE x BUTTON

            request.setAttribute("showAddForm", false);
            
            Object[] returnArray = {request, response, session, changesMade};
        
            return returnArray;
        }
        
        String inputOrganisationName = request.getParameter("newOrganisationName");
        
        ArrayList<String> errorChecking = new ArrayList<String>();

        errorChecking.add(inputOrganisationName);

        for(String input: errorChecking)
        {
            if (input.equals("") || input == null)
            {
                request.setAttribute("server_message", "All fields must be filled out in order to add an Organisation to the database.<br>Please fill out all the add organisation fields.");

                request.setAttribute("showAddForm", true);

                getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/organisationManager.jsp").forward(request, response);
            }
        }
        
        try
        {                                                    
            new OrganisationService().insert(inputOrganisationName);

            request.setAttribute("server_message", "Organisation " + inputOrganisationName + " was successfully added to the database.");

            changesMade = true;
        }
        catch (Exception ex)
        {
            if (ex.getMessage().contains("for key 'PRIMARY'"))
            {
                request.setAttribute("server_message", "Organisation " + inputOrganisationName + " already exists within the database");
            }
            Logger.getLogger(OrganisationManagerServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Object[] returnArray = {request, response, session, changesMade};
        
        return returnArray;
    }
}
