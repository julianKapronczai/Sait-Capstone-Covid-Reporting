/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.utilities;

import java.time.LocalDateTime;

/**
 * Server logic for testing the automation involved in generating covid reports
 * @author Alex Hill
 */
public class ScheduleTester implements Runnable
{
    @Override
    public void run()
    {
        LocalDateTime tempLocalDateTime = LocalDateTime.now();
        System.out.println("Testing the scheduler. The current time is: " + tempLocalDateTime);
    }
}