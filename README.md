# Spring 6 Rest MVC

Spring Boot 3 / Spring Framework 6 web application to create, retrieve, update, delete Beers and Customers from MySQL database.
It is server side (Backend) application of Beer service. Client side (Frontend) is not implemented for this app.

Application Operations: https://sfg-beer-works.github.io/brewery-api/#tag/Beer-Service

Technologies that are used in current Application:
- Data Transfer Objects (DTO) - data is transfered between Controller and Service with the help of DTOs (BeerDTO and CustomerDTO). 
- Services - process and transfer data from database to Controller and vice versa.
- MapStruct - type conversion from POJOs to Entities and vice versa is done with MapStruct dedicated converter.
- Reositories - with Spring Data JPA and Hibernate implementation methods are provided for working with the database (persist/delete/get elements).
- MySQL - database that is used in current applicaton.
- Hikari - connection pool to the database.
- Flyway - SQL database (.sql schema) initialized with Database Creation Script. Migration managed with the help of Flyway Migration Tool.
- OpenCSV - Data is loaded in DB with the help of CSV file.
- Query Parameters - Controller can accept in a query parameter to query specific data out of the database.
- Paging and Sorting - Large record sets are paged and sorted in manageable chunks.
- Jackson - used to serialize a JSON string from POJO and vice versa.
- Data validation - validation constraints are used to validate User input data, RESTful API data, data to persist to the database.
- ExceptionHandler - at the controller level, methods are used to handle exceptions and return the appropriate HTTP status to the client.
- Spring Data JPA Query Methods - query creation based on required criterias to fetch data out of the database. 
- JPA Entity Relationships - types of Relationships used in App:
customer - beer_order (one-to-many), 
beer_order - beer_order_line (one-to-many), 
beer_order_line - beer (many-to-one),
beer - category (many-to-many),
beer_order - ber_order_shipment (one-to-one).
- Tests:
for Repositories (@DataJpaTest & @SpringBootTest),
for Bootstrap - @DataJpaTest,
for Integration tests - @SpringBootTest,
for Controller tests - @WebMvcTest.

