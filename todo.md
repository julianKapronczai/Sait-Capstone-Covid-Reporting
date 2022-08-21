# To do

* change models from java.util.Date -> java.time.LocalDate
* change JPA from 2.1 -> 2.2 or 3.0
* change eclipselink from 2.x -> 3.0 or 4.0

servlet 				- pages 			- Access to 		- url

loginServlet 			- Login 			- all 				- /
ForgotPasswordServlet 	- forgotPassword 	- all 				- /forgotPassword
RegistrationSErvlet 	- registration 		- all 				- /registration
accountServlet			- account			- all(logged in)	- /secured/account
dataEntryServlet 		- dataEntry 		- reporters 		- /secured/dataEntry
viewReportServlet 		- viewReport 		- recipients 		- /secured/viewReport
dataManagerServlet 		- dataManager 		- admin 			- /secured/admin/dataManager !!!NEW!!!
userManagerServlet 		- userManager 		- admin 			- /secured/admin/userManager
adminSplashServlet 		- adminSplash 		- admin 			- /secured/admin


filter 					- url pattern

AuthenticationFilter	- /secured/*
AdminFilter				- /secured/admin/*
ReporterFilter			- /secured/dataEntry/*
RecipientFilter			- /secured/viewReport/*