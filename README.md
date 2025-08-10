# Food Ordering & Management API (Spring Boot)

A REST API for managing restaurant related tasks. This project is a work in progress, and not all features may be available yet.
I am updating the documentation as I progress through the workload, but it's fairly simple to get it up and running.

The aim of this project is for me to document my learning progress of Spring RESTful API's according to [Richardson Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html).

However, if you are a frontend developer and is in need of an API to either showcase your frontend skills or just learn like I am doing, feel free to use this API.

Domain modeled with Domain Driven Development aggregates:
-   **Restaurant, Kitchen, Product, Payment Method, Order, User, Group, Permission, City, State**.  

---

## What's currently implemented:
The project is still under development but there are some endpoints exposed already.
- CRUD operations for **Restaurants & Kitchens**: 
- CRUD operations for **States & Cities**.

## To be Implemented:
- **Upcoming** : GlobalException handler, a well establish response/request format.
- Authentication with Spring Security, JWT, OAUTH2
- Product, Order, ProductPhoto, Groups, Users, Permissions
- Some sort of deployment for ease of use.
## Tech Stack
- **Java 17+**, **Spring Boot** (Web, Validation, Data JPA)
- **MySQL 8+** (localhost)
- Build: **Maven** 

## Architecture
This section describes how the overall architecture will operate.
- **Aggregates**
    - **Restaurant** owns its **Address**, relates to a **Kitchen**, supports multiple **Payment Methods**, and contains **Products** and responsible **Users**.
    - **Product** has a **ProductPhoto** (file name, description, content type, size).
    - **Order** belongs to a **Restaurant**, a **User** (customer), and a **Payment Method**; contains **Order Items**.
    - **User ↔ Group ↔ Permission**: many-to-many for RBAC.
    - **City** belongs to a **State**.
- **Layers**
    - `api/` (controllers, DTOs, request/response models)
    - `domain/` (entities, value objects, services, exceptions)
    - `infra/` (repositories, configuration, storage adapters)

### Class Diagram : Overall Archicture
![Class Diagram](src/images/classdiagram.png)

## Getting Started

### 1) MySQL (localhost)
Assuming you have installed MySQL Workbench and have created a database, you can  add the following to your `application.properties` and Hibernate will create the tables for you.
```
spring.application.name=dbname
spring.datasource.url=jdbc:mysql://localhost/dbname?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=<username>
spring.datasource.password=<password>

spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true

```

## Run the app
#### Maven
 `./mvnw spring-boot:run`

### API Overview

### Kitchens
| Method | Endpoint               | Description       |
|--------|------------------------|-------------------|
| GET    | `/kitchens`             | List all kitchens |
| POST   | `/kitchens`             | Create a kitchen  |
| GET    | `/kitchens/{id}`        | Get a kitchen by ID |
| PUT    | `/kitchens/{id}`        | Update a kitchen  |
| DELETE | `/kitchens/{id}`        | Delete a kitchen  |

---

### Restaurants
| Method | Endpoint                 | Description         |
|--------|--------------------------|---------------------|
| GET    | `/restaurants`           | List all restaurants |
| POST   | `/restaurants`           | Create a restaurant  |
| GET    | `/restaurants/{id}`      | Get a restaurant by ID |
| PUT    | `/restaurants/{id}`      | Update a restaurant  |
| DELETE | `/restaurants/{id}`      | Delete a restaurant  |

---

### States
| Method | Endpoint                 | Description        |
|--------|--------------------------|--------------------|
| GET    | `/states`                 | List all states    |
| POST   | `/states`                 | Create a state     |
| GET    | `/states/{id}`            | Get a state by ID  |
| PUT    | `/states/{id}`            | Update a state     |
| DELETE | `/states/{id}`            | Delete a state     |

---

### Cities
| Method | Endpoint                 | Description        |
|--------|--------------------------|--------------------|
| GET    | `/cities`                 | List all cities    |
| POST   | `/cities`                 | Create a city      |
| GET    | `/cities/{id}`            | Get a city by ID   |
| PUT    | `/cities/{id}`            | Update a city      |
| DELETE | `/cities/{id}`            | Delete a city      |




## Roadmap

- [ ] **Authentication & Authorization**  
  Implement JWT-based authentication with role management via Groups/Permissions.

- [ ] **Pagination, Sorting, and Filtering**  
  Add pagination, sorting, and advanced filtering capabilities for all list endpoints.

- [ ] **File Storage Abstraction**  
  Support product photo storage with interchangeable adapters (Local / Amazon S3).

- [ ] **Input/Output DTOs & Validation**  
  Introduce DTOs for input/output with validation and standardized problem-details error responses.

- [ ] **Database Migrations**  
  Manage schema changes using Flyway for version-controlled database migrations.

- [ ] **Caching**  
  Add caching for read-heavy endpoints (e.g., kitchens, products) to improve performance.

- [ ] **Observability**  
  Implement request logging, metrics, and tracing for better monitoring and debugging.

- [ ] **Production-Ready Configuration**  
  Harden configurations and add profiles for production environments.
