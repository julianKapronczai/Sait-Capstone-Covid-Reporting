/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.servlets;

import com.sait.capstone.itsd.covidreporting.models.CovidReport;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.sait.capstone.itsd.covidreporting.services.CovidReportService;
import com.sait.capstone.itsd.covidreporting.utilities.OutputWriter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Provides server logic for the covid report viewing page
 * @author Alex Hill
 */
public class ViewReportServlet extends HttpServlet
{
    
    private List<CovidReport>  reportList = null;
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
        HttpSession session = request.getSession();
        String currentUser = (String) session.getAttribute("username");
        
        String columnSort = "";
        
        if(request.getParameter("columnSort") != null)
        {
            columnSort = request.getParameter("columnSort");
        }
        
        String sortOrder = null;
        
        if (session.getAttribute("sortOrder") != null)
        {
            String previousOrder = (String) session.getAttribute("sortOrder");
            
            if (previousOrder.equals(" DESC"))
            {
                sortOrder = " ASC";
            }
            else if (previousOrder.equals(" ASC"))
            {
                sortOrder = " DESC";
            }
        }
        else
        {
            sortOrder = " DESC";
        }
        
        session.setAttribute("sortOrder", sortOrder);
        
        try
        {
            switch (columnSort)
            {
                case "Report Date":                     this.reportList = new CovidReportService().getAllSorted("ReportDate" + sortOrder);
                                                        break;
                case "One Day Difference":              this.reportList = new CovidReportService().getAllSorted("oneDayDifference" + sortOrder);
                                                        break;
                case "Seven Day Difference":            this.reportList = new CovidReportService().getAllSorted("sevenDayDifference" + sortOrder);
                                                        break;
                case "Fourteen Day Difference":         this.reportList = new CovidReportService().getAllSorted("fourteenDayDifference" + sortOrder);
                                                        break;
                case "Total Cases":                     this.reportList = new CovidReportService().getAllSorted("totalCases" + sortOrder);
                                                        break;
                case "Total Active Cases":              this.reportList = new CovidReportService().getAllSorted("totalActiveCases" + sortOrder);
                                                        break;
                case "Total Recovered":                 this.reportList = new CovidReportService().getAllSorted("totalRecovered" + sortOrder);
                                                        break;
                case "Total Deaths":                    this.reportList = new CovidReportService().getAllSorted("totalDeaths" + sortOrder);
                                                        break;
                case "Positivity Rate":                 this.reportList = new CovidReportService().getAllSorted("positivityRate" + sortOrder);
                                                        break;                                                        
                default:                                this.reportList = new CovidReportService().getAllSorted("CovidReportID" + " ASC");
                                                        break;
            }
            session.setAttribute("reportList", this.reportList);
        }
        catch (Exception ex)
        {
            Logger.getLogger(ViewReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        getServletContext().getRequestDispatcher("/WEB-INF/base_jsps/ViewReport.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        String currentUser = (String) session.getAttribute("username");
        
        String action = request.getParameter("action");
        
        boolean changesMade = false;
        Object[] returnArray = null;
        
        switch (action)
        {
            case "download":    
                                // list of media types: https://www.iana.org/assignments/media-types/media-types.xhtml
                                response.setContentType("text/csv");
                                
                                int reportIDToDownload = Integer.parseInt((String)request.getParameter("selectedReportID"));
                                CovidReportService covidDB = new CovidReportService();
                                try 
                                {
                                    CovidReport reportToDownload = covidDB.get(reportIDToDownload);
                                    Date reportDate = reportToDownload.getReportDate();
                                    String stringifiedDate = null;

                                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                                    stringifiedDate = dateFormatter.format(reportDate);
                                                                        
                                    String filename = stringifiedDate + "_covid_report.csv";
                                    response.setHeader("Content-disposition", "attachment; filename="+filename);
                                    
                                    List<String[]> outputArray = OutputWriter.createCovidReportArray(reportToDownload);
                                    
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
                                } catch (Exception ex) {
                                    Logger.getLogger(ViewReportServlet.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                break;

        }
    }
}
