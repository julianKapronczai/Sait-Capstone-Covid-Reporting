/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.services;

import com.sait.capstone.itsd.covidreporting.utilities.EmailProviderService;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sait.capstone.itsd.covidreporting.models.Role;
import com.sait.capstone.itsd.covidreporting.models.User;
import com.sait.capstone.itsd.covidreporting.models.UuidMap;
import com.sait.capstone.itsd.covidreporting.utilities.PasswordUtilities;

/**
 * Account object handler. Performs conversion from servlet logic to database logic, such as error checking, or converting user input to matching database input
 * @author Alex Hill
 */
public class AccountService
{
    /**
     * Used to check if user login is valid
     * @param inputUsername username supplied at login
     * @param inputPassword plain text password supplied at login
     * @return username || null
     */
    public static String accountLogin(String inputUsername, String inputPassword)
    {
        try
        {
            UserService userConnection = new UserService();
            User userFromDB = userConnection.getByUsername(inputUsername);
            String storedPassword = userFromDB.getPassword();
            
            boolean validLogin = PasswordUtilities.validatePassword(inputPassword, storedPassword);
            
            if (validLogin && userFromDB.getActive())
            {
                return userFromDB.getUsername();
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    /**
     * Checks if specific user is an Admin
     * @param inputUsername username of a user
     * @return  true if username is listed as admin
     */
    public static boolean userIsAdmin(String inputUsername)
    {
        try
        {
            UserService userConnection = new UserService();
            User userFromDB = userConnection.getByUsername(inputUsername);
            Role userRole = userFromDB.getRole();
            
            boolean canManage = userRole.getCanManage();
            
            if (canManage == true)
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Checks if specific user is an Reporter
     * @param inputUsername username of a user
     * @return  true if username is listed as Reporter
     */
    public static boolean userIsReporter(String inputUsername)
    {
        try
        {
            UserService userConnection = new UserService();
            User userFromDB = userConnection.getByUsername(inputUsername);
            Role userRole = userFromDB.getRole();
            
            boolean canManage = userRole.getCanWrite();
            
            if (canManage == true)
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Checks if specific user is an Recipient
     * @param inputUsername username of a user
     * @return  true if username is listed as Recipient
     */
    public static boolean userIsRecipient(String inputUsername)
    {
        try
        {
            UserService userConnection = new UserService();
            User userFromDB = userConnection.getByUsername(inputUsername);
            Role userRole = userFromDB.getRole();
            
            boolean canManage = userRole.getCanView();
            
            if (canManage == true)
            {
                return true;
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Sends a password reset e-mail to the specified user
     * @param resetIdentifier identifies the user to send the password reset email to
     * @param identifierIsEmail true if the identifier is an email address
     * @param webINFPath path to the webINF folder
     * @param baseURL base URL of the web application
     */
    public void sendPasswordResetEmail(String resetIdentifier, boolean identifierIsEmail, String webINFPath, String baseURL)
    {
        try
        {
            User supposedUser = null;
            
            if (identifierIsEmail)
            {

                    supposedUser = new UserService().getByEmail(resetIdentifier);
                
            }
            else
            {
                    supposedUser = new UserService().getByUsername(resetIdentifier);
            }
            
            new UuidService().insert(supposedUser.getUserID());
            
            int userId = supposedUser.getUserID();
            
            String resetLink = baseURL + "?uuid=" + new UuidService().getUUID(userId);
            String destinationEmail = supposedUser.getEmail();
            String emailSubjectLine = "Jasper PharmaSave Covid Reporting System Password Reset";
            String template = webINFPath + "/email_templates/resetPasswordTemplate.html";
            
            HashMap<String, String> templateVariableMap = new HashMap<>();
            templateVariableMap.put("passwordResetLink", resetLink);
            templateVariableMap.put("username", supposedUser.getUsername());
            
            EmailProviderService.sendMail(destinationEmail, emailSubjectLine, template, templateVariableMap);
        }
        catch (Exception ex)
        {
            //Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /**
     * Performs the actual password reset started by the sendPasswordResetEmail method
     * @param hashedUUID hashed UUID associated with the user
     * @param newPassword plain text new password 
     * @return true if password was reset successfully
     */
    public boolean resetPassword(String hashedUUID, String newPassword)
    {
        UserService userConnection = new UserService();
        boolean resetSuccess = false;
        
        try
        {
            UuidMap foundMap = new UuidService().getByUuid(hashedUUID);
            
            User foundUser = foundMap.getUserID();
            
            if (foundUser != null)
            {                
                userConnection.updatePassword(foundUser.getUsername(), newPassword);
                new UuidService().delete(foundMap.getId());
                return true;
            }
        }
        catch (Exception ex)
        {
            //Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resetSuccess;
    }
    
    /**
     * Sends an account activation e-mail to the specified e-mail address
     * @param emailAddress e-mail address of the new user
     * @param webINFPath path to the webINF folder
     * @param baseURL base URL of the web application
     */
    public void sendAccountActivationEmail(String emailAddress, String organisationName, String webINFPath, String baseURL)
    {
        try
        {
            User supposedUser = null;

            supposedUser = new UserService().getByEmail(emailAddress);

            //new UuidService().insert(supposedUser.getUserID());
            
            String destinationEmail = supposedUser.getEmail();
            String emailSubjectLine = "Jasper PharmaSave Covid Reporting System Account Activation Request";
            String template = webINFPath + "/email_templates/activationEmailTemplate.html";
            
            HashMap<String, String> templateVariableMap = new HashMap<>();
            templateVariableMap.put("username", supposedUser.getUsername());
            templateVariableMap.put("loginLink", baseURL.replace("register", "home"));
            templateVariableMap.put("organisationName", organisationName);
            
            EmailProviderService.sendMail(destinationEmail, emailSubjectLine, template, templateVariableMap);
        }
        catch (Exception ex)
        {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /**
     * Sends an account activation e-mail to the specified e-mail address
     * @param emailAddress e-mail address of the new user
     * @param webINFPath path to the webINF folder
     * @param baseURL base URL of the web application
     */
    public void sendAccountActivatedEmail(String emailAddress, String webINFPath, String baseURL)
    {
        try
        {
            User supposedUser = null;

            supposedUser = new UserService().getByEmail(emailAddress);

            //new UuidService().insert(supposedUser.getUserID());
            
            String destinationEmail = supposedUser.getEmail();
            String emailSubjectLine = "Jasper PharmaSave Covid Reporting System Account Activated";
            String template = webINFPath + "/email_templates/accountActivatedEmailTemplate.html";
            
            HashMap<String, String> templateVariableMap = new HashMap<>();
            templateVariableMap.put("username", supposedUser.getUsername());
            templateVariableMap.put("loginLink", baseURL+"/home");
            
            EmailProviderService.sendMail(destinationEmail, emailSubjectLine, template, templateVariableMap);
        }
        catch (Exception ex)
        {
            Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /**
     * Performs the actual account activation as started by the sendAccountActivationEmail method
     * @param hashedUUID hashed UUId associated with the user to be activated
     * @return true if user was successfully activated in the system
     */
    public boolean activateAccount(String hashedUUID)
    {
        UserService userConnection = new UserService();
        boolean activationSuccess = false;
        
        try
        {
            UuidMap foundMap = new UuidService().getByUuid(hashedUUID);
            
            User foundUser = foundMap.getUserID();
            
            if (foundUser != null)
            {                
                foundUser.setActive(true);
                
                userConnection.updateActiveStatus(foundUser);
                
                new UuidService().delete(foundMap.getId());
                
                activationSuccess = true;
                
                return activationSuccess;
            }
        }
        catch (Exception ex)
        {
            //Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return activationSuccess;
    }
}
