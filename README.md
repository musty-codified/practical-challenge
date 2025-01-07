#  Backend Engineer Practical Challenge: Fintech-Backend-API
The backend api for a fintech application


`Built with Spring Boot, secured with Spring Security (JWT), documented with Swagger (API)`

## 1. Tools used ##
Following tools were used during the development of the API :
- **Java 17**
- **Spring Boot**
- **Maven**
- **MySQL**
- **Swagger** 
- **JWT**

---


## 2. Deployment ##
The application can be deployed on any Java Servlet container, or docker containers.

---

## 3. Running the server locally ##
-   Ensure Maven is installed and running on your machine before you run this service.
- **Clone the repository:** git clone [repo-link](https://github.com/musty-codified/practical-challenge.git)

       cd loan-mgt-service
- **Configure Environment:** Update `application.yml` with your MySQL and Memcached configurations:
- **Build the project using maven:** mvn clean install
- **Run the application from the command line:** mvn spring-boot:run
- **Access APIs: Swagger documentation is available at: http://localhost:8080/user-mgt-service/api/v1/swagger-ui.html**

## Explanation of Design Decisions ##

---
1.  Microservice Architecture
    Why: The architecture ensures modularity, scalability, and independent deployment.
   Trade-offs: Increased complexity in inter-service communication and management.
2. Database Design
   Choice: MySQL is chosen for its reliability and support for complex queries.
   Normalization: The database is normalized to avoid redundancy and ensure data consistency.
   Trade-offs: Slightly more complex schema but ensures efficient updates and queries.
3. JWT Authentication
   Why: Secure, stateless authentication for inter-service communication.
   Trade-offs: Slightly more overhead in token generation and validation.
4. Consolidated Transaction Service in Loan Service
   Why: Loan mgt service inherently includes transaction processing, and separating them adds unnecessary complexity in this use case.
   Trade-offs: Transactions are tightly coupled with loan logic.
    


## API Documentation ##
API documentation is available via Swagger:














