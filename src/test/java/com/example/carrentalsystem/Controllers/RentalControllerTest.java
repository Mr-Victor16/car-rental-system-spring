package com.example.carrentalsystem.Controllers;

import com.example.carrentalsystem.Models.ERentalStatus;
import com.example.carrentalsystem.Models.Rental;
import com.example.carrentalsystem.Payload.Request.*;
import com.example.carrentalsystem.Repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class RentalControllerTest {
    @Autowired
    private MockMvc mvc;

    private String userToken;
    private Long carID;
    private Long rentalID;

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RentalStatusRepository rentalStatusRepository;

    @Test()
    @Order(1)
    void addUser() throws Exception {
        SignupRequest signupRequest = new SignupRequest("TestUser","test@test.pl", "TestPassword", new HashSet<>(List.of("admin")));

        if(userRepository.getUserByUsername(signupRequest.getUsername()) == null){
            mvc.perform(post("/api/auth/signup").contentType(APPLICATION_JSON_VALUE).content(new ObjectMapper().writeValueAsString(signupRequest)))
                    .andExpect(status().isOk())
                    .andReturn();
        } else {
            mvc.perform(post("/api/auth/signup").contentType(APPLICATION_JSON_VALUE).content(new ObjectMapper().writeValueAsString(signupRequest)))
                    .andExpect(status().isConflict())
                    .andReturn();
        }
    }

    @Test()
    @Order(2)
    void loginUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("TestUser", "TestPassword");

        MvcResult result = mvc.perform(post("/api/auth/signin").contentType(APPLICATION_JSON_VALUE).content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        userToken = new JSONObject(result.getResponse().getContentAsString()).getString("token");
    }

    @Test()
    @Order(3)
    void addCar() throws Exception {
        AddCarRequest carRequest = new AddCarRequest();
        carRequest.setToken(userToken);
        carRequest.setMileage(123567);
        carRequest.setHorsePower(120);
        carRequest.setYear(2015);
        carRequest.setPrice(550);
        carRequest.setCapacity("2.0");
        carRequest.setFuelType(2L);
        carRequest.setBrand("Audi");
        carRequest.setModel("A3");

        mvc.perform(post("/api/cars/add").contentType(APPLICATION_JSON_VALUE).content(new ObjectMapper().writeValueAsString(carRequest)))
                .andExpect(status().isOk())
                .andReturn();

        carID = carRepository.findByMileage(123567).get(0).getId();
    }

    @Test()
    @Order(4)
    void addRental() throws Exception {
        AddCarRentalRequest carRentalRequest = new AddCarRentalRequest();
        carRentalRequest.setCarID(carID);
        carRentalRequest.setToken(userToken);
        carRentalRequest.setAddDate(LocalDate.now());
        carRentalRequest.setStartDate(LocalDate.now());
        carRentalRequest.setEndDate(LocalDate.now().plusDays(10));

        mvc.perform(post("/api/rental/add").contentType(APPLICATION_JSON_VALUE).content(
                new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(carRentalRequest)
                ))
                .andExpect(status().isOk())
                .andReturn();

        rentalID = rentalRepository.findByUser_Id(userRepository.getUserByToken(userToken).getId()).get(0).getId();
    }

    @Test()
    @Order(5)
    void getAllRentals() throws Exception {
        mvc.perform(get("/api/rental/get/all").contentType(APPLICATION_JSON_VALUE).content(userToken))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @Order(6)
    void getUserRentals() throws Exception {
        Long userID = userRepository.getUserByToken(userToken).getId();

        mvc.perform(get("/api/rental/get/user/"+userID).contentType(APPLICATION_JSON_VALUE).content(userToken))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @Order(7)
    void getRentalInfo() throws Exception {
        mvc.perform(get("/api/rental/get/"+rentalID).contentType(APPLICATION_JSON_VALUE).content(userToken))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @Order(8)
    void changeStatus() throws Exception {
        Long statusID = rentalStatusRepository.findByName(ERentalStatus.STATUS_PENDING).getId();

        mvc.perform(post("/api/rental/status/"+statusID+"/rental/"+rentalID).contentType(APPLICATION_JSON_VALUE).content(userToken))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @Order(9)
    void changeRentalInformation() throws Exception{
        EditCarRentalRequest carRentalRequest = new EditCarRentalRequest();
        carRentalRequest.setRentId(rentalID);
        carRentalRequest.setToken(userToken);
        carRentalRequest.setStartDate(LocalDate.now().plusDays(1));
        carRentalRequest.setEndDate(LocalDate.now().plusDays(5));

        mvc.perform(post("/api/rental/edit").contentType(APPLICATION_JSON_VALUE).content(
                new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(carRentalRequest)
                ))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @Order(10)
    void deleteAll() throws Exception {
        SimpleRequest rentalRequest = new SimpleRequest(rentalID, userToken);
        mvc.perform(delete("/api/rental/delete").contentType(APPLICATION_JSON_VALUE).content(new ObjectMapper().writeValueAsString(rentalRequest)))
                .andExpect(status().isOk())
                .andReturn();

        SimpleRequest carRequest = new SimpleRequest(carID, userToken);
        mvc.perform(delete("/api/cars/delete").contentType(APPLICATION_JSON_VALUE).content(new ObjectMapper().writeValueAsString(carRequest)))
                .andExpect(status().isOk())
                .andReturn();

        userRepository.deleteById(userRepository.getUserByToken(userToken).getId());
    }
}