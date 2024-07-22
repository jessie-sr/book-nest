# Book Nest
Serving as a robust and secure backend infrastructure for a bookstore application using Java Spring Boot, this project delivers a comprehensive RESTful API to manage book inventory, user transactions, and other essential bookstore functionalities. 

## Table of Contents
- [Usage Guide](#usage-guide)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Dependencies](#dependencies)
- [Testing](#testing)

## Usage Guide
1. **Clone the project**:
    ```sh
    git clone https://github.com/jessie-sr/book-nest.git
    ```

2. **Set the environmental variables (optional)**:
    1. To activate SMTP service functionality, set the following environment variables with real data. You can do this by running the following commands in the command line or changing them in the `application.properties` file:
        ```sh
        $Env:SPRING_MAIL_HOST="your_smtp_host"
        $Env:SPRING_MAIL_USERNAME="your_smtp_username"
        $Env:SPRING_MAIL_PASSWORD="your_smtp_password"
        ```
        \* Note: You can use the app without the SMTP service with some restrictions; the reset password functionality will be unavailable.
    2. The default verification link sent to the user's email address uses the default React web address (http://localhost:3000). If you want to change it, set the following environment variable:
        ```sh
        $Env:FRONT_END_URL="your_front_end_url"
        ```

3. **Run the application**:
    ```sh
    ./mvnw spring-boot:run
    ```

4. **Navigate to** `localhost:8080`

## Features
- **Restful Endpoints**: Provides RESTful API endpoints for seamless communication with the front-end application.
- **Authentication with JWT**: Implements JSON Web Token (JWT) for secure authentication between the server-side and client-side applications.
- **All Users features**:
  - Book Interactions: Browse books (all, by category, or by ID), add to cart, and place orders.
  - Account Operations: Login, signup, verification, and password reset functionalities.
  - Order Search: Search for orders by order ID and randomly generated password.
- **Authenticated Users features**:
  - Personalization: Access personal user information and view order history.
- **Admin features**:
  - Management: Monitor and manage orders, users, books, and categories for efficient bookstore administration.

## Technologies Used
- Java Spring Boot
- RESTful APIs
- JWT (JSON Web Token)
- SMTP

## Dependencies
- **spring-boot-starter-web**: Starter for building web applications using Spring MVC.
- **spring-boot-devtools**: Provides development-time tools to enhance developer productivity, including automatic application restarts.
- **commons-lang3**: Apache Commons Lang for utility functions.
- **spring-boot-starter-data-jpa**: Starter for using Spring Data JPA for database access.
- **h2**: H2 Database Engine, an in-memory relational database for development and testing purposes.
- **spring-boot-starter-data-rest**: Starter for exposing Spring Data repositories over REST.
- **spring-boot-starter-security**: Starter for enabling Spring Security and authentication/authorization features.
- **spring-boot-starter-mail**: Starter for sending emails using Spring's JavaMailSender.
- **jjwt-api**: JSON Web Token (JWT) API provided by JJWT library.
- **jjwt-impl**: Implementation of the JSON Web Token (JWT) provided by JJWT library (runtime dependency).
- **jjwt-jackson**: Jackson integration for JSON Web Token (JWT) provided by JJWT library (runtime dependency).
- **spring-boot-starter-test**: Starter for testing Spring Boot applications.
- **spring-security-test**: Spring Security testing support for integration testing.
- **rest-assured**: Rest-assured for testing RESTful APIs.
- **junit-jupiter-api**: JUnit 5 API for writing tests.
- **junit-jupiter-engine**: JUnit 5 test engine implementation.
- **spring-boot-starter-validation**: Starter for using validation in Spring Boot applications.
- **springdoc-openapi-starter-webmvc-ui**: Starter for adding OpenAPI 3 documentation and Swagger UI support to your Spring Boot application.

## Testing
### Usage Guide
Run the following command in a terminal window (in the `complete` directory):
```sh
./mvnw test
```
### Info
1. Controllers testing.
2. Repositories testing: CRUD functionalities + custom queries.
3. Rest endpoints methods testing.
