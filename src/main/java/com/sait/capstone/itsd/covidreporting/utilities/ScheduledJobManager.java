/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.utilities;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Provides back end for running tasks on a schedule
 * @author Alex Hill
 */
@WebListener
public class ScheduledJobManager implements ServletContextListener
{
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent event) 
    {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        
        // get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        // create object containing the first date time for the first initial covid report generation
        // this is the next Saturday @ 00:01
        LocalDateTime firstStartTime = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY)).withHour(0).withMinute(01);
        
        // get the difference between the current time and the start time and conver it to a long value
        Duration duration = Duration.between(currentDateTime, firstStartTime);
        long initialDelay = duration.getSeconds();
        
        // schedule the covid report generation with an initial delay equal to the calculated long value
        //  between webapp launch time and the desired first report generation
        scheduler.scheduleAtFixedRate(new AutomatedWeeklyCovidReport(), initialDelay, 24, TimeUnit.HOURS);
        
        //scheduler.scheduleAtFixedRate(new ScheduleTester(), 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) 
    {
        scheduler.shutdownNow();
    }
}
