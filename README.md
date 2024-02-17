# Car Rental System [Backend]
Car Rental System is a project for a simple car rental system that was created for the purpose of learning how to create REST APIs. 
This repository focuses on the backend part of the project. The project was implemented in a layered architecture.

The frontend for this project can be found in the repository - [car-rental-system-react](https://github.com/Mr-Victor16/car-rental-system-react)

## Technologies used
+ Spring Framework, Spring Boot, Spring Security, Spring Data JPA
+ Lombok
+ JWT
+ REST API
+ MySQL

## Features
+ **General:**
  + view cars available for rent,
  + login and registration in the system.
####
+ **User:**
  + rent a car,
  + show a list of rentals,
  + check the current rental status and rental status history,
  + show simple profile and change account password.
####
+ **Administrator:**
  + manage cars (show information, edit, change photo, delete and change visibility),
  + manage rentals (show information, edit, delete, change status),
  + manage users (show list, add, delete, change role).

##  Database schema
![db_schema_v2](https://github.com/Mr-Victor16/car-rental-system-spring/assets/101965882/e0fe0a30-f19a-4c7d-a04b-96f251c79dc7)
_The schema was created using Apache Workbench._

## How to build the project on your own
1. Clone this repository
   ```bash
    git clone https://github.com/Mr-Victor16/car-rental-system-spring
   ```
2. Go to the folder with cloned repository
3. Create an executable file for the project using Maven. Make sure you have Maven installed on your computer.
   ```bash
    mvn clean install
   ```
4. Build the Docker image with Maven
   ```bash
    docker build -t spring-backend:0.0.1 .
   ```
5. Run docker compose
   ```bash
    docker compose up
   ```

## Configuration
+ Database  
Before starting the project/create an executable file for the project using Maven, you should create a database and a database user. The data of the created database and user must be entered into the **application.properties** file:
```
spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
spring.datasource.username=<database_username>
spring.datasource.password=<database_user_password>
```

## Login details
+ Administrator  
**Username:** admin  
**Password:** admin
