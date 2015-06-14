# metapack
Author: Juan Pablo Carrera Scholz
Email: jpkarrera@gmail.com
Phone: +549-115-021-4343

Varsions:
Java v1.8
Spring Framework 4.1.6.RELEASE
JUnit v4.11
JMock v2.5.1
JSoup v1.7.2

Usage:
1) Find MetaPack-1.0.0-RELEASE.jar in Metapack/target/
2) Start the application using the command "java -jar MetaPack-1.0.0-RELEASE.jar"
URL: http:/localhost:8080/stores
Params: name -Filters results by store name
IE: 
http:/localhost:8080/stores?name=Aberdeen
http:/localhost:8080/stores?name=Aber
http:/localhost:8080/stores?name=

Application architecture:
The configuration is loaded using Spring Boot and it runs on an embedded tomcat.
I used Jsoup library to parse the HTML from the given URL.
The application is divided in layers:
-Controller
-Service
-Repository (which uses a Jsoup Service to retrieve the HTML).

If the service it's called by the first time and the response status is error, it will be shown to the user. If the error occurs once the cache store a valid response, the error will not be shown to the user, retrieving the cached data until the service returns a new valid response.
The data retrieval from service is performed manual or automatically each 30 minutes (half time of the HTML service to avoid show old data during long periods) using a @Scheduled task. The response from server is stored using a simple custom cache, long as the the response status is OK.
This logic can be improved, I preferred to shown old but not erratic data to the user.


The controller uses a content negotiation logic to retrieve Json or HTML depending on the Accept value in the request header(Json by default).
I added "Last Updated" value to the response in order to verify the age of the returned data.

Unit Tests:
I created unit tests for the Repository and Service, wich contains the main logic of the application including the cache.
