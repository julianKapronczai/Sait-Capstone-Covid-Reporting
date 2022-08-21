/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.servlets;

import com.sait.capstone.itsd.covidreporting.models.Role;
import com.sait.capstone.itsd.covidreporting.models.User;
import com.sait.capstone.itsd.covidreporting.services.AccountService;
import com.sait.capstone.itsd.covidreporting.services.RoleService;
import com.sait.capstone.itsd.covidreporting.services.UserService;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Provides server side logic for the registration page of the web-app
 * @author Alex Hill
 */
public class RegistrationServlet extends HttpServlet
{
    private List<Role> roleList = null;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            this.roleList = new RoleService().getAll();
            this.roleList.remove(0);
            
            HttpSession session = request.getSession();
            
            session.setAttribute("roleList", this.roleList);
        }
        catch (Exception e)
        {
            
        }
        
            getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/register.jsp").forward(request, response);
            return;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try 
        {
            this.roleList = new RoleService().getAll();
        } 
        catch (Exception ex) 
        {
            
        }
        
        String email = request.getParameter("registerEmail");
        String username = request.getParameter("registerUsername");
        String firstName = request.getParameter("registerFirstName");
        String lastName = request.getParameter("registerLastName");
        String rawRole = request.getParameter("registerRole");
        String desiredOrganisation = request.getParameter("registerDesiredOrganisation");
        
        int roleId = -1;
        
        try
        {
            roleId = Integer.parseInt(rawRole);
        }
        catch (Exception e)
        {
            
        }
        
        if (roleId == 1 || roleId > this.roleList.get(roleList.size()-1).getRoleId())
        {
            request.setAttribute("server_message", "Stop trying to find a way to be anything"
                                                        + " other than one of the roles listed in the drop down.");
        }
        
        try
        {
            User alreadyExists = new UserService().getByUsername(username);
            System.out.println(alreadyExists);
            if (alreadyExists != null)
            {
                request.setAttribute("registerEmail", email);
                request.setAttribute("registerFirstName", firstName);
                request.setAttribute("registerLastName", lastName);
                request.setAttribute("server_message", "A user already exists with this username"
                                                        + ", please choose another username");
                
                getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/register.jsp").forward(request, response);
                return;
            }
            
            alreadyExists = new UserService().getByEmail(email);
            
            if (alreadyExists != null)
            {
                request.setAttribute("registerEmail", email);
                request.setAttribute("registerFirstName", firstName);
                request.setAttribute("registerLastName", lastName);
                request.setAttribute("server_message", "An account has already been registered to this e-mail address"
                                                        + ", if you've forgotten your password pelase <a href='forgotpassword'>click here</a>");
                
                getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/register.jsp").forward(request, response);
                return;
            } 
        }
        catch (Exception ex)
        {
            Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        String password = request.getParameter("registerPassword");
        String confirmPassword = request.getParameter("registerConfirmPassword");
        
        if (!password.equals(confirmPassword))
        {
            request.setAttribute("registerEmail", email);
            request.setAttribute("registerUsername", username);
            request.setAttribute("registerFirstName", firstName);
            request.setAttribute("registerLastName",lastName);
            request.setAttribute("server_message", "Passwords don't match, please re-enter them into the form.");
            
            getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/register.jsp").forward(request, response);
            return;
        }
        
        try
        {
            new UserService().insertNewlyRegisteredUser(username, password, email, firstName, lastName, roleId);
        }
        catch (Exception ex)
        {
            Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String baseURL = request.getRequestURL().toString();
        String webINFPath = getServletContext().getRealPath("/WEB-INF");
        
        try 
        {
            User adminUser = new UserService().get(1);
            new AccountService().sendAccountActivationEmail(adminUser.getEmail(), desiredOrganisation, webINFPath, baseURL);
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(RegistrationServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        request.setAttribute("server_message", "Thank you for registering for the Jasper PharmaSave Covid Reporting System."
                                                + " You will receive an e-mail once the administrators have activated your"
                                                + " account.");
        
        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/register.jsp").forward(request, response);
        return;
    }
}
