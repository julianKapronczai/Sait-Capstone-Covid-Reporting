/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.sait.capstone.itsd.covidreporting.services.AccountService;

/**
 * Provides server logic for the login page
 * @author Alex Hill
 */
public class LoginServlet extends HttpServlet
{
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
        
        if (session.getAttribute("server_message") != null)
        {
            request.setAttribute("server_message", (String) session.getAttribute("server_message"));
        }

        session.invalidate(); // just by going to the login page the user is logged out :-) 
        
        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/Login.jsp").forward(request, response);
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
        
        String inputUsername = request.getParameter("loginUsername");
        String inputPassword = request.getParameter("loginPassword");

        String validatedUsername = AccountService.accountLogin(inputUsername, inputPassword);

        if (validatedUsername == null) 
        {
            getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/Login.jsp").forward(request, response);
            return;
        }
        
        boolean isAdmin = AccountService.userIsAdmin(validatedUsername);
        
        if (isAdmin == true)
        {
            session.setAttribute("showAdminLink", true);          
        }
        
        boolean isReporter = AccountService.userIsReporter(validatedUsername);
        
        if (isReporter == true)
        {
            session.setAttribute("showReporterLink", true);
        }
        
        boolean isRecipient = AccountService.userIsRecipient(validatedUsername);
        
        if (isRecipient == true)
        {
            session.setAttribute("showRecipientLink", true);
        }
        
        //capitalise the first letter in the username, for better display.
        validatedUsername = validatedUsername.substring(0,1).toUpperCase() + validatedUsername.substring(1);
        
        session.setAttribute("username", validatedUsername);     
        
        if (isAdmin == true)
        {
            response.sendRedirect("secured/admin");
        }
        else if (isReporter == true)
        {
            response.sendRedirect("secured/dataEntry");
        }
        else if (isRecipient == true)
        {
            response.sendRedirect("secured/viewReport");
        }
        else
        {
            response.sendRedirect("home");
        }
        
        return;
    }
}
