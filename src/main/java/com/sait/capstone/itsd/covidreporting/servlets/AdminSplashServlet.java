/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.servlets;

import com.sait.capstone.itsd.covidreporting.data_access_layer.CovidReportDB;
import com.sait.capstone.itsd.covidreporting.models.CovidReport;
import com.sait.capstone.itsd.covidreporting.models.CovidTestResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sait.capstone.itsd.covidreporting.services.CovidReportService;
import com.sait.capstone.itsd.covidreporting.services.CovidTestResultService;
import com.sait.capstone.itsd.covidreporting.utilities.CovidReportGenerator;
import com.sait.capstone.itsd.covidreporting.utilities.OutputWriter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


/**
 * Provides server logic for the Admin splash page
 * @author Alex Hill
 */
public class AdminSplashServlet extends HttpServlet
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
        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/adminSplashPage.jsp").forward(request, response);
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
        Object[] returnArray = null;
        
        String destinationPage = request.getParameter("adminSubmit");
        
        switch (destinationPage)
        {
            case "User Management":         response.sendRedirect("admin/userManager");
                                            break;
            case "Organisation Management": response.sendRedirect("admin/organisationManager");
                                            break;
            case "Generate Report":         try
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

                                                // Send the new report to be downloaded as a CSV immediately
                                                // list of media types: https://www.iana.org/assignments/media-types/media-types.xhtml
                                                response.setContentType("text/csv");

                                                String stringifiedDate = null;

                                                LocalDateTime tempLocalCurrentDate = LocalDateTime.now();
                                                Date currentDate = Date.from(tempLocalCurrentDate.atZone(ZoneId.systemDefault()).toInstant());
                                                
                                                // set the current date since it will be empty as not being pulled from database
                                                completeCovidReport.setReportDate(currentDate);
                                                
                                                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

                                                stringifiedDate = dateFormatter.format(currentDate);

                                                String filename = "Forced_generation_" + stringifiedDate + "_covid_report.csv";
                                                response.setHeader("Content-disposition", "attachment; filename="+filename);

                                                List<String[]> outputArray = OutputWriter.createCovidReportArray(completeCovidReport);
                                                
                                                for (String[] s : outputArray)
                                                {
                                                    System.out.println(s);
                                                }

                                                InputStream reportInput = new ByteArrayInputStream(OutputWriter.downloadWriter(outputArray));
                                                OutputStream downloadOutput = response.getOutputStream();

                                                byte[] buffer = new byte[4096];

                                                int numBytesRead;
                                                do
                                                {
                                                    numBytesRead = reportInput.read(buffer);
                                                    downloadOutput.write(buffer, 0, numBytesRead);

                                                }while (numBytesRead != -1);
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                            return;
            default:                        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/adminSplashPage.jsp").forward(request, response);
        }
    }
}
