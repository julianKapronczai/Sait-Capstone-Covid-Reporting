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
        <meta name="viewport" content="width=device-width,initial-scale=1.0">
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

        <h1>Covid Reporting</h1>
        
        <h2>${server_message}</h2>
        
        
        
        <table id='outerTable'>
            <tr><td>
            <c:if test="${showEditForm}">
                <c:out  value=   "<table id='editTable'><form action='' method='post' autocomplete='off'><tr><th class='addFormHeader'>Edit Test Result  <input type='submit' name='closeForm' value='close' class='material-icons-round rightIconAlign plainIcon'/></th></tr>"
                        escapeXml = "false">
                </c:out>
            </c:if>
            <c:if test="${showAddForm}">
                <c:out  value=   "<table id='newTable'><form action='' method='post' autocomplete='off'><tr><th class='addFormHeader'>Add Test Result <input type='submit' name='closeForm' value='close' class='material-icons-round rightIconAlign plainIcon'/></th></tr>"
                        escapeXml = "false">
                </c:out>
            </c:if>
            <c:if test="${!showAddForm and !showEditForm}">
                <c:out  value=   ""
                        escapeXml = "false">
                </c:out>
            </c:if>
            <c:if test="${showAddForm}">
                <c:out value=   "
                                    <tr>
                                                        <td>
                                                            <input type='date' name='newResultDate' id='newResultDate' value='${newResultDate}' autocomplete='nope'/>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='number' name='newTestsTaken' id='newTestsTaken' value='${newTestsTaken}' placeHolder='Number of new tests Taken' step='1'/>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='number' name='newTestsPositive' id='newTestsPositive' value='${newTestsPositive}' placeHolder='Number of new Positive tests' step='1'/>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='number' name='newTestsInconclusive' id='newTestsInconclusive' value='${newTestsInconclusive}' placeHolder='Number of inconclusive tests' step='1'/>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <textarea name='newNotes' id='newNotes' placeHolder='Write notes here'>${newNotes}</textarea>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='submit' value='Add Item' class='modifyButton'>
                                                            <input type='hidden' name='action' value='addResult'>
                                                        </td>
                                                    </tr>
                                                 </table>
                                            </form>
                                "
                                escapeXml = "false">
                </c:out>
                </c:if>                
                <c:if test="${showEditForm}">
                    <fmt:formatDate var="EditresultDate" pattern= "yyyy-MM-dd" value="${editResultDate}" />
                    <c:out value=   "
                           <tr id=labelsedit>
                                                    <td id=labelsedit>
                                                    <label for='editResultDate'>Date of Report</label>
                                                    </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='date' name='editResultDate' id='editResultDate' value='${EditresultDate}' autocomplete='nope'/>
                                                        </td>
                                                    </tr>
                                                    <tr id=labelsedit>
                                                    <td id=labelsedit>
                                                    <label for='editNumberTaken'>Number of new tests Taken</label>
                                                    </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='number' name='editNumberTaken' id='editNumberTaken' value='${editNumberTaken}' step='1'/>
                                                        </td>
                                                    </tr>
                                                    <tr id=labelsedit>
                                                        <td id=labelsedit>
                                                        <label for='editNumberPositive'>Number of new Positive tests</label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='number' name='editNumberPositive' id='editNumberPositive' value='${editNumberPositive}' step='1'/>
                                                        </td>
                                                    </tr>
                                                    <tr id=labelsedit>
                                                        <td id=labelsedit>
                                                        <label for='editNumberInconclusive'>Number of inconclusive tests</label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='number' name='editNumberInconclusive' id='editNumberInconclusive' value='${editNumberInconclusive}' step='1'/>
                                                        </td>
                                                    </tr>
                                                    <tr id=labelsedit>
                                                        <td id=labelsedit>
                                                        <label for='editNotes'>Write notes here</label>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <textarea name='editNotes' id='editNotes' placeHolder='Write notes here'>${editNotes}</textarea>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <input type='submit' value='Update Item' class='modifyButton'/>
                                                            <input type='hidden' name='action' value='updateResult'/>
                                                            <input type='hidden' name='selectedTestResultID' value='${selectedTestResultID}'/>
                                                        </td>
                                                    </tr>
                                                 </table>
                                            </form>
                                "
                                    escapeXml = "false">
                    </c:out>
                </c:if>  
            
            </td></tr>
            <tr class='smallSpacerRow'></tr>
            <tr>    
                <td>
                    <table id='displayTable'>
                        <thead>
                        <tr class="largeFont">    
                            <th class="headerRow" colspan="9">Covid Reports</th> 
                        </tr>
                        <tr class="headerRow">
                            <th class="dateWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortResultDate" value="Result Date" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="dateWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortSubmissionDate" value="Submission Date" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="numberOfWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortTestsTaken" value="Number of Tests Taken" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="numberOfWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortPositive" value="Number of Positive Tests" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="numberOfWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortInconclusive" value="Number of Inconclusive Tests" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="positvityRateWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortPositivityRate" value="Positivity Rate" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="largeWidth">
                                <form action="" method="get">
                                    <input type="submit" name="columnSort" id="columnSortNotes" value="Notes" class="plainText"/><!--<label for="columnSortActive" class="material-icons-round textAligned">sort_by_alpha</label>-->
                                </form>
                            </th>
                            <th class="smallWidth">Edit</th>
                            <th class="smallWidth">Delete</th>
                        </tr>
                        </thead>
                        <c:if test="${!showAddForm}">
                            <c:out value=   "
                                               <tr id='secondHeaderRow' class='secondHeaderRow'>
                                                    <td colspan='9'>
                                                            <form action='' method='post'>
                                                                    <label for='addItemButton' class='material-icons textAligned largeIcon'>add_circle_outline</label>
                                                                    <input type='submit' id='addItemButton' name='addItemButton' value ='Add Item'>
                                                                    <input type='hidden' name='action' value='displayAddForm'>
                                                            </form>
                                                    </td>
                                                </tr>
                                            " 
                                   escapeXml = "false">
                            </c:out>
                        </c:if>
                        <c:forEach items="${testResultList}" var="list">
                            <fmt:formatDate var="resultDate" type="date" value="${list.getResultDate()}" />
                            <fmt:formatDate var="submissionDate" type="date" value="${list.getSubmissionDate()}" />
                            <fmt:formatNumber var="positivityRatePercentage" type="PERCENT" value="${list.getPositivityRate()}"/>
                            <c:out value=   " 
                                                <tr>
                                                    <form action='' method='post'>                               
                                                    <td data-label='Result Date' class='small'>${resultDate}</td>
                                                    <td data-label='Submission Date'>${submissionDate}</td>
                                                    <td data-label='# tests taken'>${list.getNumberTestsTaken()}</td>
                                                    <td data-label='# tests Positive'>${list.getNumberTestsPositive()}</td>
                                                    <td data-label='# tests inconclusive'>${list.getNumberTestsInconclusive()}</td>
                                                    <td data-label='Positivity Rate'>${positivityRatePercentage}</td>
                                                    <td data-label='Notes'>${list.getNotes()}</td>
                                                    <td data-label='Edit'>
                                                        <input type='submit' name='action' id='edit' class='material-icons' value='edit'>
                                                    </td>
                                                    <td data-label='Delete'>
                                                        <input type='submit' name='action' id='delete' value='delete' class='material-icons red'>
                                                        <input type='hidden' name='selectTestResult' id='selectTestResult' value='${list.getCovidTestResultID()}'>
                                                    </td>
                                                </form>
                                            </tr>
                                            <tr class='mobileSpacerRow'></tr>
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