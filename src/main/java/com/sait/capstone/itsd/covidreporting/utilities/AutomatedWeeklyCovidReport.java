/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.utilities;

import com.sait.capstone.itsd.covidreporting.data_access_layer.CovidReportDB;
import com.sait.capstone.itsd.covidreporting.models.CovidReport;
import com.sait.capstone.itsd.covidreporting.models.CovidTestResult;
import com.sait.capstone.itsd.covidreporting.models.User;
import com.sait.capstone.itsd.covidreporting.services.CovidReportService;
import com.sait.capstone.itsd.covidreporting.services.CovidTestResultService;
import com.sait.capstone.itsd.covidreporting.services.UserService;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides server logic for controlling the automatic generation of covid reports every week
 * @author Alex Hill
 */
public class AutomatedWeeklyCovidReport implements Runnable
{
    @Override
    public void run()
    {
        try 
        {
            // Fetch a new ID for a CovidReport
            int newReportId = new CovidReportDB().getNewCovidReportId();

            // Create a new CovidReport
            CovidReport newCovidReport = new CovidReport(newReportId);
            
            // Subtract 7 days from current date to use to fetch all covidTestResults within the past week
            LocalDateTime tempLocalDateTime = LocalDateTime.now().minusDays(7);
            Date reportStartDate = Date.from(tempLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
            
            // Fetch all the CovidTestResults from the start date
            List<CovidTestResult> previousWeeksTestResults = new CovidTestResultService().getAllAfterDate(reportStartDate, "CovidTestResultID ASC");
            
            // Attach the list of fetched CovidTestResults to the new CovidReport
            newCovidReport.setCovidTestResultList(previousWeeksTestResults);
            
            // Fetch last CovidReport from database
            CovidReport previousCovidReport = new CovidReportService().get(newReportId-1);
            
            // Pass both CovidReports to the CovidReportGenerator utility
            CovidReportGenerator reportGenerator = new CovidReportGenerator(previousCovidReport, newCovidReport);
            
            // Generate the new report fields
            CovidReport completeCovidReport = reportGenerator.generateCovidReport();
            
            // Run error checking to make sure new CovidReport has been populated correctly
            // might be done in CovidReportGenerator
            
            
            // Insert the new CovidReport into the database
            new CovidReportService().insert(completeCovidReport);

            // Send out e-mail to all recipients saying a new report has been generated and is available to be viewed.
            this.sendAlertEmail();
        } 
        catch (Exception ex) 
        {
            Logger.getLogger(AutomatedWeeklyCovidReport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sends an e-mail to all recipients alerting them to a new report within the system.
     */
    public void sendAlertEmail()
    {
        try
        {
            List<User> allUsers = new UserService().getAll();
            
            List<String> recipientEmails = new ArrayList();
            
            for (User tempUser : allUsers)
            {
                if (tempUser.getRole().getCanView())
                {
                    recipientEmails.add(tempUser.getEmail());
                }
            }
            
            String emailSubjectLine = "Jasper PharmaSave Covid Reporting System Account Activation Request";
            
            URL fileLocation = AutomatedWeeklyCovidReport.class.getClassLoader().getResource("../email_templates/newReportGeneratedTemplate.html");
            String template = fileLocation.toString();
            
            HashMap<String, String> templateVariableMap = new HashMap<>();
            templateVariableMap.put("username", "to all recipients.");
            
            EmailProviderService.sendReportAlert(recipientEmails, emailSubjectLine, template, templateVariableMap);
        }
        catch (Exception ex)
        {
            Logger.getLogger(AutomatedWeeklyCovidReport.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}
