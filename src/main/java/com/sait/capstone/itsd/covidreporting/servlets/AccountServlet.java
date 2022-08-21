/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.sait.capstone.itsd.covidreporting.models.User;
import com.sait.capstone.itsd.covidreporting.services.AccountService;
import com.sait.capstone.itsd.covidreporting.services.UserService;

/**
 * Provides server logic for the Account page
 * @author Alex Hill
 */
public class AccountServlet extends HttpServlet
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
        Object[] returnArray = this.populateAccountInformation(request, response, session);
        
        request = (HttpServletRequest) returnArray[0];
        response = (HttpServletResponse) returnArray[1];
        session =  (HttpSession) returnArray[2];
        
        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/account.jsp").forward(request, response);
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
        Object[] returnArray = this.populateAccountInformation(request, response, session);
        
        request = (HttpServletRequest) returnArray[0];
        response = (HttpServletResponse) returnArray[1];
        session =  (HttpSession) returnArray[2];
        
        String action = request.getParameter("action");
        
        switch(action)
        {
            case "deactivate":          this.deactivateAccount(request, response, session);
                                        break;
                                        
            case "deactivateConfirm":   this.confirmDeactivateAccount(request, response, session);
                                        break;
                                        
            case "accountChange":
                                        this.accountChange(request, response, session);
                                        break;
            
            case "passwordChange":      
                                        this.changePassword(request, response, session);
                                        break;
            
            case "updatePassword":
                                        this.updatePassword(request, response, session);
                                        break;
                                        
            default:
                                        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/account.jsp").forward(request, response);
                                        break;
        }
    }
    
    /**
     * fetches the account information for the current user
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private Object[] populateAccountInformation(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        try
        {
            User currentUser = new UserService().getByUsername((String) session.getAttribute("username"));
            
            request.setAttribute("email", currentUser.getEmail());
            request.setAttribute("firstName", currentUser.getFirstName());
            request.setAttribute("lastName", currentUser.getLastName());
            
        }
        catch (Exception ex)
        {
            Logger.getLogger(AccountServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Object[] returnArray = {request, response, session};
        
        return returnArray;
    }
    
    /**
     * performs logic for deactivating a user account
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private void deactivateAccount(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        request.setAttribute("server_message", "<span class='material-icons'>warning</span>Warning<br>once deactivated your account can only be reactivated by a System Administrator."
                                                                                + "<br>Enter your password below and then click the button to deactivate your account.");
            
        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/confirmDeactivation.jsp").forward(request, response);
    }
    
    /**
     * provides logic for confirmation for deactivating the user account
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private void confirmDeactivateAccount(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        String username = (String) session.getAttribute("username");
        String password = request.getParameter("deactivatePassword");

        String confirmedUsername = new AccountService().accountLogin(username, password);

        if (confirmedUsername.equalsIgnoreCase(username))
        {
            User userToDeactivate;

            try 
            {
                userToDeactivate = new UserService().getByUsername(confirmedUsername);
                userToDeactivate.setActive(false);

                new UserService().updateActiveStatus(userToDeactivate);
            }
            catch (Exception ex) {
                Logger.getLogger(AccountServlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            response.sendRedirect("../home");
        }
        else
        {
            request.setAttribute("server_message", "<span class='material-icons'>warning</span>Warning<br>once deactivated your account can only be reactivated by a System Administrator."
                                                + "<br>Enter your password below and then click the button to deactivate your account."
                                                + "<br><br><br><span class='red'>Incorrect Password, please retype your password.</span>");

            getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/confirmDeactivation.jsp").forward(request, response);
        }
    }
    
    /**
     * provides logic for changing aspects of the user account
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private void accountChange(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        String updatedEmail = request.getParameter("accountEmail");
        try
        {
            if (updatedEmail != null && new UserService().getByEmail(updatedEmail) != null)
            {
                request.setAttribute("server_message", "Invalid e-mail, please try again.");

                getServletContext().getRequestDispatcher("/WEB-INF/account.jsp").forward(request, response);
            }

            String updatedFirstName = request.getParameter("accountFirstName");
            String updatedLastName = request.getParameter("accountLastName");

            User currentUser = new UserService().getByUsername((String) session.getAttribute("username"));

            if (updatedFirstName != null && !updatedFirstName.equals("") && !updatedFirstName.equals(" "))
            {
                currentUser.setFirstName(updatedFirstName);
            }

            if (updatedLastName != null && !updatedLastName.equals("") && !updatedLastName.equals(" "))
            {
                currentUser.setLastName(updatedLastName);
            }

            if (updatedEmail != null && updatedEmail.contains("@") && updatedEmail.contains("."))
            {
                currentUser.setEmail(updatedEmail);
            }

            new UserService().update(currentUser);

            request.setAttribute("server_message", "User information successfully updated.");
        }
        catch (Exception ex)
        {
            Logger.getLogger(AccountServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        Object[] returnArray = this.populateAccountInformation(request, response, session);

        request = (HttpServletRequest) returnArray[0];
        response = (HttpServletResponse) returnArray[1];
        session =  (HttpSession) returnArray[2];
        
        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/account.jsp").forward(request, response);
    }
    
    /**
     * provides logic for changing a users password
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private void changePassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        request.setAttribute("server_message", "To change your account password please complete the below form.");
        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/updatePassword.jsp").forward(request, response);
    }
    
    /**
     * provides back-end logic for performing the actual password change within the system
     * @param request
     * @param response
     * @param session
     * @return
     * @throws ServletException
     * @throws IOException 
     */
    private void updatePassword(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException
    {
        String currentPassword = request.getParameter("updatePasswordCurrent");
        String currentUsername = (String) session.getAttribute("username");

        String verifiedUsername = new AccountService().accountLogin(currentUsername, currentPassword);

        if (verifiedUsername == null)
        {
            request.setAttribute("server_message", "To change your account password please complete the below form."
                                                    + "<br><br><br><span class='red'>Incorrect current password</span>");

            getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/updatePassword.jsp").forward(request, response);
        }
        else
        {
            String newPassword = request.getParameter("updatePasswordNew");
            String newPasswordConfirm = request.getParameter("updatePasswordConfirm");

            if (!newPassword.equals(newPasswordConfirm))
            {
                request.setAttribute("server_message", "To change your account password please complete the below form."
                                                    + "<br><br><br><span class='red'>New passwords don't match.</span>");

                getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/updatePassword.jsp").forward(request, response);
            }
            else
            {
                try
                {
                    User userToUpdate = new UserService().getByUsername(verifiedUsername);

                    new UserService().updatePassword(userToUpdate.getUsername(), newPassword);

                    request.setAttribute("server_message", "Account password successfully updated. All future logins will require the use of the new password.");


                    getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/account.jsp").forward(request, response);
                }
                catch (Exception ex)
                {
                    Logger.getLogger(AccountServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
