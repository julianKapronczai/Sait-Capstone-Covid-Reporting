/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.utilities;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *class used to handle all functions surrounding user passwords
 * @author Alex Hill
 */
public class PasswordUtilities
{    
    /***********************HASHING METHODS**************************/
    /**
     * generates a hash value for a given plain text input
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public static String generatePasswordHash(String password) 
                                                throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 10000;
        char[] passwordCharArray = password.toCharArray();
        byte[] saltByteArray = getSalt();

        PBEKeySpec keySpec = new PBEKeySpec(passwordCharArray, saltByteArray, iterations, 64 * 8);
        
        SecretKeyFactory secretFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

        byte[] hash = secretFactory.generateSecret(keySpec).getEncoded();
        return iterations + ":" + toHex(saltByteArray) + ":" + toHex(hash);
    }

    /**
     * generates a salt value
     * @return
     * @throws NoSuchAlgorithmException 
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        
        byte[] salt = new byte[16];
        
        secureRandom.nextBytes(salt);
        
        return salt;
    }

    /**
     * converts a byte array into a hex value
     * @param inputByteArray
     * @return
     * @throws NoSuchAlgorithmException 
     */
    private static String toHex(byte[] inputByteArray) throws NoSuchAlgorithmException
    {
        BigInteger byteArrayToBigInt = new BigInteger(1, inputByteArray);
        
        String inputToHex = byteArrayToBigInt.toString(16);

        int paddingLength = (inputByteArray.length * 2) - inputToHex.length();
        
        if(paddingLength > 0)
        {
            return String.format("%0"  + paddingLength + "d", 0) + inputToHex;
        }
        else
        {
            return inputToHex;
        }
    }
    
    /***********************VAlIDATION METHODS**************************/
    /**
     * compares provided plaintext password with the hashed password within the database
     * @param originalPassword
     * @param storedPassword
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public static boolean validatePassword(String originalPassword, String storedPassword) 
                                                    throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        String[] saltPasswordCombo = storedPassword.split(":");
        
        int iterations = Integer.parseInt(saltPasswordCombo[0]);

        byte[] storedSalt = fromHex(saltPasswordCombo[1]);
        byte[] storedPasswordHash = fromHex(saltPasswordCombo[2]);

        PBEKeySpec keySpec = new PBEKeySpec(originalPassword.toCharArray()
                                            , storedSalt, iterations, storedPasswordHash.length * 8);
        
        SecretKeyFactory secretFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        
        byte[] calculatedHash = secretFactory.generateSecret(keySpec).getEncoded();

        for (int i = 0; i < storedPasswordHash.length && i < calculatedHash.length; i++)
        {
        	if (storedPasswordHash[i] != calculatedHash[i])
        	{
        		return false;
        	}
        }
        
        return true;
    }
    
    /**
     * converts hex to a byte array
     * @param inputHex
     * @return
     * @throws NoSuchAlgorithmException 
     */
    private static byte[] fromHex(String inputHex) throws NoSuchAlgorithmException
    {
        byte[] convertedByteArray = new byte[inputHex.length() / 2];
        
        for(int i = 0; i < convertedByteArray.length ;i++)
        {
            String extractedHexValue = inputHex.substring(2 * i, 2 * i + 2);
            convertedByteArray[i] = (byte) Integer.parseInt(extractedHexValue, 16);
        }
        
        return convertedByteArray;
    }
}
