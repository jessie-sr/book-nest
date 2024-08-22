# Book Nest

Aa a comprehensive bookstore application backend written in Java using the Spring Boot framework, this project provides a robust set of RESTful APIs that handle user interactions and data management for books, orders, and user accounts, including authentication and authorization. The system is designed to be secure, scalable, and maintainable, leveraging Spring Boot's powerful features to achieve these goals.

## Architecture Overview

- **Controllers**: Handle HTTP requests and responses, interacting with the service layer to execute business logic operations and return data to clients.
- **Services**: Contain the core business logic, interacting with repositories to manage data storage and retrieval.
- **Repositories**: Abstract the data layer, providing clean interaction with the database using Spring Data JPA.
- **Models**: Represent the application's data model and define the database schema through JPA.
- **Security Configuration**: Manages authentication and authorization, ensuring secure access to resources.
- **Utilities and Helpers**: Provide support functionalities like email sending and password encryption.
- **Tests**: Ensure that the application functions correctly and meets its design specifications.

## Technologies Used
- Java Spring Boot
- RESTful APIs
- JWT (JSON Web Token)
- SMTP
  
## Dependencies
DependenciesListed in the pom.xml file, major dependencies include:
- Spring Boot (Web, Security, Data JPA, Mail)
- H2 Database
- JJWT for JWT handling
  
## Usage Guide

1. **Clone the project**:
    ```sh
    git clone https://github.com/jessie-sr/book-nest.git
    ```

2. **Set the environmental variables (optional)**:
    - Activate SMTP service functionality by setting the following environment variables:
      ```sh
      $Env:SPRING_MAIL_HOST="your_smtp_host"
      $Env:SPRING_MAIL_USERNAME="your_smtp_username"
      $Env:SPRING_MAIL_PASSWORD="your_smtp_password"
      ```
      \*Note: The app can be used without the SMTP service, but reset password functionality will be unavailable.
    - Customize the verification link sent to users by setting the `FRONT_END_URL` environment variable:
      ```sh
      $Env:FRONT_END_URL="your_front_end_url"
      ```

3. **Run the application**:
    ```sh
    ./mvnw spring-boot:run
    ```

4. **Access the application** at `localhost:8080`.

## Testing

### Usage Guide
Run the following command to execute tests:
```sh
./mvnw test
```

### Info
1. Controllers testing.
2. Repositories testing: CRUD functionalities and custom queries.
3. REST endpoints methods testing.
