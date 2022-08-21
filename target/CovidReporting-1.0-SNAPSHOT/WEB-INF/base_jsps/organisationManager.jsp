<%-- 
    Document   : Admin
    Created on : 11-Nov-2021, 14:36:06
    Author     : BritishWaldo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/userManager_style.css">
        <link href="https://fonts.googleapis.com/css?family=Material+Icons|Material+Icons+Round" rel="stylesheet">
        <title>Jasper PharmaSave Covid Reporting</title>
    </head>
    <body>
        <main>
            <ul id="navBar">
                <li><a class="logout"  href="../../home">Logout</a></li>
                <li><a class="active" href="../admin">Admin</a></li>
                <li><a href="../dataEntry">Data Entry Management</a></li>
                <li><a href="../viewReport">Report Viewer</a></li>
                <li id="menuRight"><a href="../account" class="account">Welcome, ${username}</a></li>
            </ul>

            <h1>Organisation Management Dashboard</h1>
            
            <h2>${server_message}</h2>

            <table id='outerTable'>
                <tr><td>
                <c:if test="${showAddForm}">
                    <c:out  value=   "<table id='newTable'><form action='' method='post' autocomplete='off'><tr><th class='addFormHeader'>Add Organisation <input type='submit' name='closeForm' value='close' class='material-icons-round rightIconAlign plainIcon'/></th>"
                            escapeXml = "false">
                    </c:out>
                </c:if>
                <c:if test="${!showAddForm}">
                    <c:out  value=   ""
                            escapeXml = "false">
                    </c:out>
                </c:if>
                <c:if test="${showAddForm}">
                    <c:out value=   "
                                                        <tr>
                                                            <td>
                                                                <input type='text' name='newOrganisationName' id='newOrganisationName' value='${newOrganisationName}' placeHolder='Organisation Name' autocomplete='nope'/>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td>
                                                                <input type='submit' value='Add Organisation' class='modifyButton'>
                                                                <input type='hidden' name='action' value='addOrganisation'>
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </form>
                                            </td>
                                        </tr>
                                        " 
                            escapeXml = "false">
                        </c:out>
                    </c:if>                
                    </td></tr>
                <tr class='smallSpacerRow'></tr>
                <tr>    
                    <td>
                        <table id='displayTable'>
                            <tr class="largeFont" >    
                                <th class="headerRow" colspan="1">Current Organisations</th> 
                            </tr>
                            <tr class="headerRow">
                                <th>
                                    <form action="" method="get">
                                        <input type="submit" name="columnSort" id="columnSortActive" value="Organisation Name" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                    </form>
                                </th>
                            </tr>
                            <c:if test="${!showAddForm}">
                                <c:out value=   "
                                                <tr class='secondHeaderRow'>
                                                        <td colspan='1'>
                                                                <form action='' method='post'>
                                                                        <label for='displayAddForm' class='material-icons textAligned largeIcon'>add_business</label>
                                                                        <input type='submit' id='displayAddForm' name='displayAddForm' value ='Add Organisation' class='plainText'>
                                                                        <input type='hidden' name='action' value='displayAddForm'>
                                                                </form>
                                                        </td>
                                                    </tr>
                                                " 
                                    escapeXml = "false">
                                </c:out>
                            </c:if>
                            <c:forEach items="${organisationList}" var="organisation">
                                <c:out value=   " 
                                                    <tr>
                                                        <form action='' method='post'>    
                                                            <td>${organisation.getOrganisationName()}</td>
                                                        </form>
                                                    </tr>
                                                "
                                                escapeXml = "false">
                                </c:out>
                            </c:forEach>
                        </table>
                    </td>
                </tr>
            </table>
        </main>
        <footer class="centered">
            *Click on table header to toggle between sort options.
        </footer>
    </body>
</html>

