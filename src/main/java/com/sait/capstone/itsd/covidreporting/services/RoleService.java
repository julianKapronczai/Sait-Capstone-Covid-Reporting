/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.services;

import com.sait.capstone.itsd.covidreporting.data_access_layer.RoleDB;
import java.util.List;
import com.sait.capstone.itsd.covidreporting.models.Role;

/** 
 * Role object handler. Performs conversion from servlet logic to database logic, such as error checking, or converting user input to matching database input
 * @author Alex Hill
 */
public class RoleService
{
    /**
     * fetches a specific role from the database
     * @param inputRoleID the unique ID of a role within the database
     * @return the Role object associated with the specified ID number
     * @throws Exception 
     */
    public Role get(int inputRoleID) throws Exception 
    {
        RoleDB roleConnection = new RoleDB();
        Role tempRole = roleConnection.get(inputRoleID);
        return tempRole;
    }
    
    /**
     * fetches a list of all roles within the database
     * @return a list of all roles within the database
     * @throws Exception 
     */
    public List<Role> getAll() throws Exception 
    {
        RoleDB roleConnection = new RoleDB();
        List<Role> allRolesList = roleConnection.getAll();
        return allRolesList;
    }
    
   /**
    * inserts a new role into the database
    * @param inputRoleID the unique id number of a role
    * @param inputRoleName the name of the role
    * @param inputCanWrite true if the role has write permissions
    * @param inputCanView true if the role has view permissions
    * @param inputCanManage true if the role has management permissions
    * @throws Exception 
    */
    public void insert(String inputRoleName, boolean inputCanWrite, boolean inputCanView, boolean inputCanManage) throws Exception 
    {
        RoleDB roleConnection = new RoleDB();
        int tempNewId = roleConnection.getNewRoleId();
        Role tempRole = new Role(tempNewId, inputRoleName, inputCanWrite, inputCanView, inputCanManage);
        roleConnection.insert(tempRole);
    }
    
    /**
     * updates an existing role within the database
     * @param inputRoleID the unique id number of a role
     * @param inputRoleName the name of a role
     * @throws Exception 
     */
    public void update(int inputRoleID, String inputRoleName) throws Exception 
    {
        RoleDB roleConnection = new RoleDB();
        Role tempRole = this.get(inputRoleID);
        
        if (!tempRole.getRoleName().equals(inputRoleName))
        {
            tempRole.setRoleName(inputRoleName);
        }
        
        roleConnection.update(tempRole);
    }
    
    /**
     * deletes a specific role from the database
     * @param inputRoleID the unique id number of a role
     * @throws Exception 
     */
    public void delete(int inputRoleID) throws Exception 
    {
        RoleDB roleConnection = new RoleDB();
        Role tempRole = new Role(inputRoleID);
        roleConnection.update(tempRole);
    }
    
    /**
     * fetches a role from the database that matches the provided lookup string
     * @param lookupString a string that contains the name of a valid role within the system.
     * @return the unique ID associated with the specified string
     * @throws Exception 
     */
    public int roleIDLookup(String lookupString) throws Exception
    {
        RoleDB roleConnection = new RoleDB();
        
        Role tempRole = roleConnection.roleIDLookup(lookupString);
        
        return tempRole.getRoleId();
    }    
}
