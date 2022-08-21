/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.services;

import com.sait.capstone.itsd.covidreporting.data_access_layer.UuidDB;
import com.sait.capstone.itsd.covidreporting.models.User;
import com.sait.capstone.itsd.covidreporting.models.UuidMap;
import java.util.UUID;
import com.sait.capstone.itsd.covidreporting.utilities.PasswordUtilities;

/**
 * UuidMap object handler. Performs conversion from servlet logic to database logic, such as error checking, or converting user input to matching database input
 * @author Alex Hill
 */
public class UuidService
{
    /**
     * fetches the username associated with a Id
     * @param inputUuidId
     * @return
     * @throws Exception 
     */
    public String getUsername(int inputUuidId) throws Exception 
    {
        UuidDB uuidConnection = new UuidDB();
        UuidMap tempUuid = uuidConnection.get(inputUuidId);
        
        UserService userService = new UserService();
        User tempUser = userService.get(tempUuid.getId());
        
        return tempUser.getUsername();       
    }
    
    /**
     * fetches the UUID associated with a specific user
     * @param inputUserID
     * @return
     * @throws Exception 
     */
    public String getUUID(int inputUserID) throws Exception 
    {
        UuidDB uuidConnection = new UuidDB();
        UuidMap tempUuid = uuidConnection.getByUserID(inputUserID);
        
        return tempUuid.getUuid();
    }
    
    /**
     * fetches the UuidMap associated with a specific UUID
     * @param inputUuid
     * @return
     * @throws Exception 
     */
    public UuidMap getByUuid(String inputUuid) throws Exception 
    {
        UuidDB uuidConnection = new UuidDB();
        UuidMap tempUuid = uuidConnection.getByUuid(inputUuid);
        
        return tempUuid;
    }
    
    /**
     * inserts a new UuidMap into the database
     * @param inputUserID
     * @throws Exception 
     */
    public void insert(int inputUserID) throws Exception 
    {
        UuidDB uuidConnection = new UuidDB();
        
        String rawUUID = UUID.randomUUID().toString();
        String hashedUUID = PasswordUtilities.generatePasswordHash(rawUUID);
        String[] splitHashedUuid = hashedUUID.split(":");
        
        int newId = uuidConnection.getNewUuidId();
        
        UuidMap tempUuidMap = new UuidMap(newId, Integer.parseInt(splitHashedUuid[0]), splitHashedUuid[1], splitHashedUuid[2]);
                
        User tempUser = new UserService().get(inputUserID);
        
        tempUuidMap.setUserID(tempUser);
        
        uuidConnection.insert(tempUuidMap);
    }
    
    /**
     * deletes a UuidMap from the database
     * @param hashedUUID
     * @throws Exception 
     */
    public void delete(int inputUuidId) throws Exception 
    {
        UuidDB uuidConnection = new UuidDB();
        
        UuidMap tempUuid = uuidConnection.get(inputUuidId);
        
        uuidConnection.delete(tempUuid);        
    }
}
