/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sait.capstone.itsd.covidreporting.utilities;

import com.opencsv.CSVWriter;
import com.sait.capstone.itsd.covidreporting.models.CovidReport;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides server logic for allowing the downloading of files.
 * @author Alex Hill
 */
public class OutputWriter 
{
    //https://stackoverflow.com/questions/34972466/exporting-database-to-csv-file-with-java    
    //https://github.com/eugenp/tutorials/blob/master/javax-servlets/src/main/java/com/baeldung/servlets/DownloadServlet.java
    //https://www.baeldung.com/servlet-download-file
    public static byte[] downloadWriter(List<String[]> input)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        //create stream writer to allow writing to stream
        OutputStreamWriter test = new OutputStreamWriter(outputStream);
        //create CSVWriter to convert result set into csv format
        CSVWriter csvToStream = new CSVWriter(test);
        
        try
        {
            //write the entire result set to the byte array stream aka "servlet input"
            csvToStream.writeAll(input);
            csvToStream.flush();
            //return the byte array that was just written
            return outputStream.toByteArray();
            
            //In servlet, implement download method from links above
            //use returned value from this method as the input for the download
            /*
                To get this to work the logic is as follows:
                    1. get the report ID from the report page
                    2. use the report ID to fetch the report from the DB as a resultset
                    3. pass that resultSet to this method
                    4. set the content type to match the output (csv, text, etc)
                    5. use the pipedInputStream returned from this method as the input stream in the downloader servlet
                    6. HttpResponse outputStream will read the pipedInputStream and pass it back to the browser as an attachment
            */
        }
        catch (Exception e)
        {
               e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Creates the required format to enable the downloading of the supplied CovidReport
     * @param inputReport
     * @return 
     */
    public static List<String[]> createCovidReportArray (CovidReport inputReport)
    {
        List<String[]> csvOutput = new ArrayList();
        
        String[] tempArray = {"Report Date", "Seven Day Difference", "Fourteen Day Difference", "Total Cases", "Total Active Cases", "Total Recovered Cases", "Positivity Rate", "Notes"};
        
        csvOutput.add(tempArray);
        
        csvOutput.add(inputReport.toStringArray());

        return csvOutput;
    }
}
