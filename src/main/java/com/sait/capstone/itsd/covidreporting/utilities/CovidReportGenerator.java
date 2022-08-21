/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.utilities;

import com.sait.capstone.itsd.covidreporting.models.CovidReport;
import com.sait.capstone.itsd.covidreporting.models.CovidTestResult;
import java.util.List;

/**
 * Class used to generate a covid report, including all data calculations
 * @author Alex Hill
 */
public class CovidReportGenerator 
{
    private CovidReport previousCovidReport;
    private CovidReport newCovidReport;
    
    /**
     * generic constructor
     */
    public CovidReportGenerator()
    {
        
    }
    
    /**
     * full constructor 
     * @param previousCovidReport
     * @param newCovidReport 
     */
    public CovidReportGenerator (CovidReport previousCovidReport, CovidReport newCovidReport)
    {
        this.previousCovidReport = previousCovidReport;
        this.newCovidReport = newCovidReport;
    }
    
    /**
     * method used to run all back-end logic required with generating a covid report
     * @return 
     */
    public CovidReport generateCovidReport()
    {
        // check if new report actually has any test results attached
        List<CovidTestResult> tempResultList = this.newCovidReport.getCovidTestResultList();
        
        if (tempResultList == null || tempResultList.isEmpty())
        {
            // return null so that a proper error can be displayed via the servlet
            return null;
        }
        else
        {
            // do all the generation
            
            // seven day difference must be done first, or logic breaks for other sections
            this.newCovidReport.setSevenDayDifference(this.calculateSevenDayDifference());
            
            this.newCovidReport.setOneDayDifference(this.calculateOneDayDifference());
            
            this.newCovidReport.setFourteenDayDifference(this.calculateFourteenDayDifference());
            
            this.newCovidReport.setTotalCases(this.calculateTotalCases());
            
            this.newCovidReport.setTotalRecovered(this.calculateTotalRecovered());
            
            this.newCovidReport.setTotalActiveCases(this.calculateTotalActiveCases());
            
            this.newCovidReport.setTotalDeaths(this.calculateTotalDeaths());
            
            this.newCovidReport.setPositivityRate(this.calculatePositivityRate());
        }

        return newCovidReport;
    }
    
    /**
     * calculates the one day difference in covid cases
     * placeholder for if tests become a daily thing instead of weekly
     * @return 
     */
    private int calculateOneDayDifference()
    {
        return 0;
    }
    
    /**
     * calculates the seven day difference in covid cases
     * @return 
     */
    private int calculateSevenDayDifference()
    {
        int previousTotal = this.previousCovidReport.getTotalCases();
        int newTotal = 0;
        
        for (CovidTestResult result : this.newCovidReport.getCovidTestResultList())
        {
            newTotal += result.getNumberTestsPositive();
        }
       
        return newTotal - previousTotal;
    }
    
    /**
     * calculates the fourteen day difference in covid cases
     * Pre-condition: Must have already calculated the 7 day difference
     * @return 
     */
    private int calculateFourteenDayDifference()
    {
        // get 7 day difference from previous report
        int previous7DayDifference = this.previousCovidReport.getSevenDayDifference();
        
        // add on all the new positive cases in the last week
        int fourteenDayDifference = previous7DayDifference + this.newCovidReport.getSevenDayDifference();
        
        return fourteenDayDifference;
    }
    
    /**
     * calculates the total number of covid cases
     * Pre-condition: Must have already calculated the 7 day difference
     * @return 
     */
    private int calculateTotalCases()
    {
        int totalCases = this.previousCovidReport.getTotalCases();
        
        totalCases += this.newCovidReport.getSevenDayDifference();
        
        return totalCases;
    }
    
    /**
     * calculates the total number of recovered covid cases
     * @return 
     */
    private int calculateTotalRecovered()
    {
        // total number of cases - (previous 14 day difference -  previous seven day difference)
        // recovered = positive test result thats > 14 days old
        
        int newlyRecovered = this.newCovidReport.getTotalCases();
        
        newlyRecovered -= (this.previousCovidReport.getFourteenDayDifference() - this.previousCovidReport.getSevenDayDifference());
        
        int totalRecovered = this.previousCovidReport.getTotalRecovered() + newlyRecovered;
                
        return totalRecovered;
    }
    
    /**
     * calculates the total number of active covid cases
     * @return 
     */
    private int calculateTotalActiveCases()
    {
        int totalActive = this.newCovidReport.getTotalCases() - this.newCovidReport.getTotalRecovered() - this.newCovidReport.getTotalDeaths();
        
        return totalActive;
    }
    
    /**
     * calculates the total number of deaths due to covid
     * placeholder if testResults ever include followup information
     * @return 
     */
    private int calculateTotalDeaths()
    {
        return 0;
    }
    
    /**
     * calculates the current positivity rate of covid tests
     * @return 
     */
    private double calculatePositivityRate()
    {
        int totalTests = 0;
        int totalPositive = 0;
        
        for (CovidTestResult result : this.newCovidReport.getCovidTestResultList())
        {
            totalTests += result.getNumberTestsTaken() - result.getNumberTestsInconclusive();
            totalPositive += result.getNumberTestsPositive();
        }
        
        double positivityRate = (totalPositive * 1.0)/(totalTests * 1.0);
        
        return positivityRate;
    }
}
