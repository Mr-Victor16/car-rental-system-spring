# Car Rental System [Backend]
Car Rental System is a project showcasing a simple car rental system. 
It has been designed using a layered architecture, enabling effective management of business logic, presentation and data access. 

This repository contains only the backend part of the project.\
For the frontend part of the project, please visit the repository - [car-rental-system-react](https://github.com/Mr-Victor16/car-rental-system-react)

## Technologies used
+ Spring Framework, Spring Boot, Spring Security, Spring Data JPA
+ Lombok
+ JWT
+ REST API
+ MySQL
+ Docker, Docker Compose

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

## Running the project with Docker Compose
1. Clone this repository
   ```bash
    git clone https://github.com/Mr-Victor16/car-rental-system-spring
   ```
2. Go to the folder with cloned repository
3. Run docker compose
   ```bash
    docker compose up
   ```

## Login details
+ Administrator  
**Username:** admin  
**Password:** admin  
<br />
+ User  
**Username:** user  
**Password:** password