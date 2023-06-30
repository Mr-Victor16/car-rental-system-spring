# Car Rental System [Backend]
Car Rental System is a project for a simple car rental system that was created for the purpose of learning how to create REST APIs. 
This repository focuses on the backend part of the project.

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
  + car rental,
  + show a list of your rentals,
  + check the current rental status and rental status history,
  + show your simple profile and change your account password.
####
+ **Administrator:**
  + manage cars (show information, edit, change photo, delete and change visibility),
  + manage rentals (show information, edit, delete, change status).

## Configuration
+ Database  
Before starting the project, you should create a database and a database user. The data of the created database and user must be entered into the **application.properties** file:
```agsl
spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
spring.datasource.username=<database_username>
spring.datasource.password=<database_user_password>
```

## Login details
+ Administrator  
**Username:** admin  
**Password:** admin