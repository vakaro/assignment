###ASSIGNMENT MANUAL

###Prerequisites & Assumptions
 
Project is built using Spring Boot Framework, taking advantage of Spring Security (for Basic Auth), Spring Data for peristance in the H2 in-memory db and Spring Web for Rest capabilities. Swagger plugin was used for basic documentation of the API.
<br><br>
Timezones are handled by the JVM settings.
Users will be able to update/delete only their consultations.

In order to achieve microservices transition for analytics, a new controller in a separate package was created. Interaction with business objects occurs through HTTP calls, so theoretically most of the code can be reused.

### Environment 

You simply need to have JAVA 11+ installed as well as gradle version 5+. 
###How to run

Once project is checked out, in the root folder execute : **./gradlew bootRun** 
<br> 
For endpoints needing authentication simply pass your base64 encoded username password in the request. eg.
```
curl --location --request POST 'http://localhost:8080/api/consultant/add' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YW5lbWFpbEBhbmVtYWlsLmNvbToxMjM0NTY3' \
--data-raw '{
    "fullName": "Kostas Pap",
    "email":"anemail@anemail.com",
    "description" : "A new consultant"
}'
```

H2 database can be examined while app is running at: [H2 DB](http://localhost:8080/h2-console/) 
###Improvements

* More unit tests
* More data validation
* Elaborate on exception handling since development time was very limited. 
###Swagger Documentation

Once application is up and running basic swagger documentation with exposed endpoints can be found :
* [Swagger-UI](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/)

