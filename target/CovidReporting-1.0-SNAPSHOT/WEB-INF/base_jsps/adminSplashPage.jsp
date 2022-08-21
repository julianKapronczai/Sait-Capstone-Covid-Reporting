<%-- 
    Document   : adminSplashPage
    Created on : 21-Nov-2021, 10:44:30
    Author     : BritishWaldo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/styles/admin_style.css">
        <link href="https://fonts.googleapis.com/css?family=Material+Icons|Material+Icons+Round" rel="stylesheet">
        <title>Jasper PharmaSave Covid Reporting</title>
    </head>
    <body>
        <main>
            <ul id="navBar">
                    <li><a class="logout"  href="../home">Logout</a></li>
                    <li><a class="active" href="admin">Admin</a></li>
                    <li><a href="dataEntry">Data Entry Management</a></li>
                    <li><a href="viewReport">Report Viewer</a></li>
                    <li id="menuRight"><a href="../account" class="account">Welcome, ${username}</a></li>
            </ul>

            <h1>Administrative Dashboard</h1>

            <table>
                <tr>
                    <td>
                        <form method="post" action="">
                            <input type="submit" name="adminSubmit" id="adminSubmit" value="User Management"/>
                        </form>
                    </td>
                </tr>
                <tr class="bigSpacerRow"></tr>
                <tr>
                    <td>
                        <form method="post" action="">
                            <input type="submit" name="adminSubmit" id="adminSubmit" value="Organisation Management"/>
                        </form>
                    </td>
                </tr>
                <tr class="bigSpacerRow"></tr>
                <tr>
                    <td>
                        <form method="post" action="">
                            <input type="submit" name="adminSubmit" id="adminSubmit" value="Generate Report">
                        </form>
                    </td>
                </tr>
            </table>
        </main>
        <footer class="centered">
        </footer>
    </body>
</html>
