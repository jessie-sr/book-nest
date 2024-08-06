The project's codebase is divided into several folders, each serving distinct roles within the application. 

### 1. **httpforms**
This folder contains classes that are used to handle data transfer objects (DTOs). These DTOs are crucial for receiving and sending data between the client and server in a structured format. Each class in this folder represents a specific form or data structure that the application's front end may submit or request.

### 2. **model**
This directory hosts the domain entities used in the application. Each class represents a table in the database and is typically annotated with JPA annotations to describe the relationship between the object fields and the database columns. Here are some key classes:
- [`User`](src/main/java/com/pro/mybooklist/model/User.java): Represents a user entity.
- [`Book`](src/main/java/com/pro/mybooklist/model/Book.java): Represents a book entity.
- [`Order`](src/main/java/com/pro/mybooklist/model/Order.java): Represents an order entity.

These entities are central to the application's functionality, allowing for operations such as queries, inserts, updates, and deletions through the repository interfaces.

### 3. **service**
The service folder contains classes that define the business logic of the application. Services interact with the repository layer to fetch or update data and perform operations specific to the business requirements. Some key services include:
- [`UserService`](src/main/java/com/pro/mybooklist/service/UserService.java): Manages operations related to users.
- [`BookService`](src/main/java/com/pro/mybooklist/service/BookService.java): Handles book-related business logic.
- [`OrderService`](src/main/java/com/pro/mybooklist/service/OrderService.java): Manages order processing and logic.

The services ensure that the business rules of the application are adhered to and provide a clear separation between the web layer and data access layers.

### 4. **sqlforms**
This directory contains classes that are used to structure complex queries or custom data retrieval operations not directly handled by simple CRUD repository methods. These classes are typically used to handle custom results from SQL queries, such as aggregated data or specific projections that don't fit directly into the standard entity models.

### 5. **web**
The web folder contains controllers that handle HTTP requests. These controllers use the services to perform operations based on the incoming requests and send responses back to the client. There are typically different controllers for different aspects of the application:
- [`RestPublicController`](src/main/java/com/pro/mybooklist/web/RestPublicController.java): Manages public-facing (unauthenticated) endpoints.
- [`RestAuthenticatedController`](src/main/java/com/pro/mybooklist/web/RestAuthenticatedController.java): Handles endpoints that require user authentication.
- [`RestAdminController`](src/main/java/com/pro/mybooklist/web/RestAdminController.java): Dedicated to administrative functions and endpoints accessible only by users with administrative privileges.