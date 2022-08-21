/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.services;

import com.sait.capstone.itsd.covidreporting.data_access_layer.OrganisationDB;
import com.sait.capstone.itsd.covidreporting.data_access_layer.RoleDB;
import com.sait.capstone.itsd.covidreporting.data_access_layer.UserDB;
import com.sait.capstone.itsd.covidreporting.models.Organisation;
import java.util.List;
import com.sait.capstone.itsd.covidreporting.models.Role;
import com.sait.capstone.itsd.covidreporting.models.User;
import com.sait.capstone.itsd.covidreporting.utilities.PasswordUtilities;

/** 
 * User object handler. Performs conversion from servlet logic to database logic, such as error checking, or converting user input to matching database input
 * @author Alex Hill
 */
public class UserService
{    
    /**
     * fetches a specific user from the database
     * @param inputUserID the unique id of a user
     * @return the User object associated with the specified Id number
     * @throws Exception 
     */
    public User get(int inputUserID) throws Exception 
    {
        UserDB userConnection = new UserDB();
        User tempUser = userConnection.getById(inputUserID);
        return tempUser;
    }
    
    /**
     * fetches a specific user from the database using its username
     * @param inputUsername the username of a valid user within the system
     * @return the User object associated with the specified username
     * @throws Exception 
     */
    public User getByUsername(String inputUsername) throws Exception 
    {
        UserDB userConnection = new UserDB();
        User tempUser = userConnection.getByUsername(inputUsername);
        return tempUser;
    }
    
    /**
     * fetches a specific user from the database using its e-mail address
     * @param inputEmail the e-mail of a valid user within the system
     * @return the User object associated with the specified e-mail address.
     * @throws Exception 
     */
    public User getByEmail(String inputEmail) throws Exception 
    {
        UserDB userConnection = new UserDB();
        User tempUser = userConnection.getByEmail(inputEmail);
        return tempUser;
    }
    
    /**
     * fetches a list of all users within the database
     * @return a list of all users within the database
     * @throws Exception 
     */
    public List<User> getAll() throws Exception 
    {
        UserDB userConnection = new UserDB();
        List<User> allUsersList = userConnection.getAll();
        return allUsersList;
    }
    
    /**
     * fetches and sorts a list of all users in the database
     * @param columnName a specific column to sort the resultant list by
     * @return a list of all users sorted by the specified column
     * @throws Exception 
     */
    public List<User> getAllSorted(String columnName) throws Exception
    {
        UserDB userConnection = new UserDB();
        List<User> allUsersListSorted = userConnection.getAllSorted(columnName);
        return allUsersListSorted;
    }
    
    /**
     * inserts a new user into the database
     * @param newUsername username of new user
     * @param newPassword plain text password of the new user
     * @param newEmail e-mail address of the new user
     * @param newFirstName first name of the new user
     * @param newLastName last name of the new user
     * @param newIsActive true if the new user is to be active within the system
     * @param newUserRoleID the unique ID number of the role the new user should have
     * @throws Exception 
     */
    public void insert(String newUsername, String newPassword, String newEmail, String newFirstName
                        , String newLastName, int newOrganisationID, boolean newIsActive, int newUserRoleID) throws Exception 
    {
        UserDB userConnection = new UserDB();
        
        String generatedPasswordHash = PasswordUtilities.generatePasswordHash(newPassword);
        int tempNewId = userConnection.getNewUserId();
        
        User tempUser = new User(tempNewId, newUsername, generatedPasswordHash, newEmail
                                    , newFirstName, newLastName, newIsActive);
        
        RoleDB roleConnection = new RoleDB();
        Role tempRole = roleConnection.get(newUserRoleID);
        tempUser.setRole(tempRole);
        
        OrganisationDB organisationConnection = new OrganisationDB();
        Organisation tempOrganisation = organisationConnection.get(newOrganisationID);
        tempUser.setOrganisation(tempOrganisation);
        
        userConnection.insert(tempUser);
    }
    
    /**
     * inserts a newly registered user account in the database
     * @param newUsername username of new user
     * @param newPassword plain text password of the new user
     * @param newEmail e-mail address of the new user
     * @param newFirstName first name of the new user
     * @param newLastName last name of the new user
     * @throws Exception 
     */
    public void insertNewlyRegisteredUser(String newUsername, String newPassword, String newEmail
                                            , String newFirstName, String newLastName, int roleId) throws Exception 
    {
        UserDB userConnection = new UserDB();
        
        String generatedPasswordHash = PasswordUtilities.generatePasswordHash(newPassword);
        int tempNewId = userConnection.getNewUserId();
        
        User tempUser = new User(tempNewId, newUsername, generatedPasswordHash, newEmail,
                                    newFirstName, newLastName, false);
        
        RoleService roleConnection = new RoleService();
        Role tempRole = roleConnection.get(roleId);
        
        tempUser.setRole(tempRole);
        
        OrganisationService organisationConnection = new OrganisationService();
        // the "unassigned" organisation will always have an id of 1 within the database.
        Organisation tempOrganisation = organisationConnection.get(1);
        
        tempUser.setOrganisation(tempOrganisation);
        
        userConnection.insert(tempUser);
    }
    
    /**
     * updates an existing user within the database, but doesn't touch the password
     * @param originalUsername the username of the User as it stands within the database
     * @param newUsername new username of the user
     * @param newEmail new e-mail address of the the user
     * @param newFirstName new first name of the the user
     * @param newLastName new last name of the the user
     * @param newIsActive true if the user is to be active within the system
     * @param newUserRoleID the new unique ID number of the role the  user should have
     * @throws Exception 
     */
    public void update(String originalUsername, String newUsername, String newEmail
                        , String newFirstName, String newLastName, int organisation, boolean newIsActive
                        , int newUserRoleID) throws Exception 
    {
        UserDB userConnection = new UserDB();
        User tempUser = this.getByUsername(originalUsername);
        
        if (!tempUser.getUsername().equals(newUsername))
        {
            tempUser.setUsername(newUsername);
        }
        
        if (!tempUser.getEmail().equals(newEmail))
        {
            tempUser.setEmail(newEmail);
        }
        
        if (!tempUser.getFirstName().equals(newFirstName))
        {
            tempUser.setFirstName(newFirstName);
        }
        
        if (!tempUser.getLastName().equals(newLastName))
        {
            tempUser.setLastName(newLastName);
        }
        if(!tempUser.getOrganisation().equals(organisation))
        {
            OrganisationDB orgConnection = new OrganisationDB();
            Organisation updateOrg = orgConnection.get(organisation);
            tempUser.setOrganisation(updateOrg);
        }
        if (tempUser.getActive() != newIsActive)
        {
            tempUser.setActive(newIsActive);
        }
        
        if (tempUser.getRole().getRoleId() != newUserRoleID)
        {
            RoleDB roleConnection = new RoleDB();
            Role tempRole = roleConnection.get(newUserRoleID);
            tempUser.setRole(tempRole);
        }
        
        userConnection.update(originalUsername, tempUser);
    }
    
    /**
     * updates every aspect of an existing user within the database
     * @param originalUsername the username of the User as it stands within the database
     * @param newUsername new username of the user
     * @param newPassword new plain text password of the user
     * @param newEmail new e-mail address of the the user
     * @param newFirstName new first name of the the user
     * @param newLastName new last name of the the user
     * @param newIsActive true if the user is to be active within the system
     * @param newUserRoleID the new unique ID number of the role the  user should have
     * @throws Exception 
     */
    public void update(String originalUsername, String newUsername, String newPassword
                        , String newEmail, String newFirstName, String newLastName,int organisation, boolean newIsActive
                        , int newUserRoleID) throws Exception 
    {
        UserDB userConnection = new UserDB();
        User tempUser = this.getByUsername(originalUsername);
        
        String generatedPasswordHash = PasswordUtilities.generatePasswordHash(newPassword);
        
        if (!tempUser.getUsername().equals(newUsername))
        {
            tempUser.setUsername(newUsername);
        }
        
        if (!tempUser.getEmail().equals(newEmail))
        {
            tempUser.setEmail(newEmail);
        }
        
        if (!tempUser.getPassword().equals(generatedPasswordHash))
        {
            tempUser.setPassword(generatedPasswordHash);
        }
        
        if (!tempUser.getFirstName().equals(newFirstName))
        {
            tempUser.setFirstName(newFirstName);
        }
        
        if (!tempUser.getLastName().equals(newLastName))
        {
            tempUser.setLastName(newLastName);
        }
        
        if (tempUser.getActive() != newIsActive)
        {
            tempUser.setActive(newIsActive);
        }
        
        if (tempUser.getRole().getRoleId() != newUserRoleID)
        {
            RoleDB roleConnection = new RoleDB();
            Role tempRole = roleConnection.get(newUserRoleID);
            tempUser.setRole(tempRole);
        }
        if(!tempUser.getOrganisation().equals(organisation))
        {
            OrganisationDB orgConnection = new OrganisationDB();
            Organisation updateOrg = orgConnection.get(organisation);
            tempUser.setOrganisation(updateOrg);
        }
        
        userConnection.update(originalUsername, tempUser);
    }
    
    /**
     * updates a user within the database using a User object
     * @param userToUpdate the User object to update
     * @throws Exception 
     */
    public void update(User userToUpdate) throws Exception 
    {
        UserDB userConnection = new UserDB();

        userConnection.update(userToUpdate.getUsername(), userToUpdate);
    }
    
    /**
     * flips the active status of a user within the database
     * @param userToUpdate the User object to update
     * @throws Exception 
     */
    public void updateActiveStatus(User userToUpdate) throws Exception 
    {
        UserDB userConnection = new UserDB();

        userConnection.updateActiveStatus(userToUpdate);
    }

    /**
     * updates the password of a user within the database
     * @param username username of the User object to update
     * @param newPassword the new plain text password of the user
     * @throws Exception 
     */
    public void updatePassword(String username, String newPassword) throws Exception 
    {
        UserDB userConnection = new UserDB();
        User tempUser = this.getByUsername(username);
        
        String generatedPasswordHash = PasswordUtilities.generatePasswordHash(newPassword);
        tempUser.setPassword(generatedPasswordHash);
        
        userConnection.updatePassword(tempUser);
    }
    
    /**
     * deletes a specific user within the database
     * @param inputUsername the username of the User object to delete
     * @throws Exception 
     */
    public void delete(String inputUsername) throws Exception 
    {
        UserDB userConnection = new UserDB();
        User tempUser = this.getByUsername(inputUsername);
        userConnection.delete(tempUser);
    }    
}
