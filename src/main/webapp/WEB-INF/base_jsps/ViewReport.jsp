<%-- 
    Document   : inventory
    Created on : 11-Nov-2021, 14:37:55
    Author     : BritishWaldo
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/data_entry_style.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons|Material+Icons+Round">
        <title>Jasper PharmaSave Covid Reporting</title>
    </head>
    <body>
        <ul id="navBar">
            <li><a class="logout" href="../home">Logout</a></li>
            <c:if test="${showAdminLink}">
                <c:out value = "
            <li><a href='admin'>Admin</a></li>
                                " escapeXml = "false">
                </c:out>
            </c:if>
            <li id="menuRight"><a href="account" class="account">Welcome, ${username}</a></li>
        </ul>

        <h1>Covid Reports</h1>
        
        <h2>${server_message}</h2>
        
        
        
        <table id='outerTable'>
            <tr><td>
            </td></tr>
            <tr class='smallSpacerRow'></tr>
            <tr>    
                <td>
                    <table id='displayTable'>
                        <tr class="largeFont">    
                            <th class="headerRow" colspan="10">Covid Reports</th> 
                        </tr>
                        <tr class="headerRow">
                            <th class="mediumWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortReportDate" value="Report Date" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="mediumWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortOneDay" value="One Day Difference" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="mediumWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortSevenDay" value="Seven Day Difference" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="mediumWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortFourteenDay" value="Fourteen Day Difference" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="mediumWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortTotalCases" value="Total Cases" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="mediumWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortActiveCases" value="Total Active Cases" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="mediumWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortTotalRecovered" value="Total Recovered" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="mediumWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortTotalDeaths" value="Total Deaths" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="mediumWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortPositivityRate" value="Positivity Rate" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="mediumWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortNotes" value="Notes" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="mediumWidth">
                                Download Report
                            </th>
                        </tr>
                        <c:forEach items="${reportList}" var="list">
                            <fmt:formatDate var="reportDate" type="date" value="${list.getReportDate()}" />
                            <fmt:formatNumber var="positivityRatePercentage" type="PERCENT" value="${list.getPositivityRate()}"/>
                            <c:out value=   " 
                                                <tr>
                                                    <form action='' method='post'>                               
                                                    <td class='small'>${reportDate}</td>
                                                    <td>${list.getOneDayDifference()}</td>
                                                    <td>${list.getSevenDayDifference()}</td>
                                                    <td>${list.getFourteenDayDifference()}</td>
                                                    <td>${list.getTotalCases()}</td>
                                                    <td>${list.getTotalActiveCases()}</td>
                                                    <td>${list.getTotalRecovered()}</td>
                                                    <td>${list.getTotalDeaths()}</td>
                                                    <td>${positivityRatePercentage}</td>
                                                    <td>
                                            "
                                                escapeXml = "false">
                            </c:out>
                                                <c:forEach items="${list.getCovidTestResultList()}" var="result">
                                                    <c:out value = "
                                                                    ${result.getNotes()}
                                                                    <br>
                                                                   "
                                                                        escapeXml = "false">
                                                    </c:out>
                                                </c:forEach>
                            <c:out value=   "
                                                    </td>
                                                    <td>
                                                        <input type='submit' name='action' id='download' value='download' class='material-icons red'>
                                                        <input type='hidden' name='selectedReportID' id='selectedReportID' value='${list.getCovidReportID()}'>
                                                    </td>
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
    </body>
</html>