/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sait.capstone.itsd.covidreporting.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * Provides basic authentication filtering for all servlets
 * @author Alex Hill
 */
public class AuthenticationFilter implements Filter
{
    /**
     * Initialises the filter.
     * @param filterConfig
     * @throws ServletException 
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }
    
    /**
     * Contains the logic for executing the function of the filter. In this case making sure the logged in user is a valid user.
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException 
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        //before dofilter = request
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession httpSession = httpRequest.getSession();
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String username = (String) httpSession.getAttribute("username");
        
        if (username == null)
        {
            //deals with posibility that user might try to access deeper sections of the admin functionality
            //if there is more than one forward slash in the requested url then it boots them back a level to access inventory
            //solves issue with redirect pointing to a none existent inventory url within the admin section
            
            String requestURI = httpRequest.getRequestURI();
            requestURI = requestURI.substring(1);
            
            //requestURI = requestURI.substring(requestURI.indexOf('/'));

            int firstSlashIndex = requestURI.indexOf('/');
            int lastSlashIndex = requestURI.lastIndexOf('/');
            
            if (firstSlashIndex != lastSlashIndex)
            {
                httpResponse.sendRedirect("../login");
                return;
            }
            else
            {
                httpResponse.sendRedirect("/login");
            return;
            }
        }

        //calls next filter in chain or loads servlet
        chain.doFilter(request, response);

        //after dofilter = response
    }
    
    /**
     * destroys the filter.
     */
    @Override
    public void destroy()
    {

    }
}
