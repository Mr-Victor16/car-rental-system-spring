package com.example.carrentalsystem.controllers;

import com.example.carrentalsystem.models.RentalStatusEnum;
import com.example.carrentalsystem.models.Rental;
import com.example.carrentalsystem.payload.request.*;
import com.example.carrentalsystem.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
class RentalControllerTest {
    @Autowired
    private MockMvc mvc;

    private String userToken;
    private Long userID;
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
        userID = userRepository.getUserByUsername("TestUser").getId();
        System.out.println(userID);
    }

    @Test()
    @Order(3)
    void addCar() throws Exception {
        AddCarRequest carRequest = new AddCarRequest();
        carRequest.setMileage(123567);
        carRequest.setHorsePower(120);
        carRequest.setYear(2015);
        carRequest.setPrice(550);
        carRequest.setCapacity("2.0");
        carRequest.setFuelType(2L);
        carRequest.setBrand("Audi");
        carRequest.setModel("A3");

        mvc.perform(post("/api/car").contentType(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                        .content(new ObjectMapper().writeValueAsString(carRequest)))
                .andExpect(status().isOk())
                .andReturn();

        carID = carRepository.findByMileage(123567).get(0).getId();
    }

    @Test()
    @Order(4)
    void addRental() throws Exception {
        AddCarRentalRequest rentalRequest = new AddCarRentalRequest(carID, userID, LocalDate.now(), LocalDate.now(), LocalDate.now().plusDays(10));

        mvc.perform(post("/api/rental").contentType(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                        .content(
                            new ObjectMapper()
                                    .registerModule(new JavaTimeModule())
                                    .writeValueAsString(rentalRequest)
                            )
                )
                .andExpect(status().isOk())
                .andReturn();

        rentalID = rentalRepository.findByUserId(userID).get(0).getId();
    }

    @Test()
    @Order(5)
    void getAllRentals() throws Exception {
        mvc.perform(get("/api/rentals")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @Order(6)
    void getUserRentals() throws Exception {
        mvc.perform(get("/api/rentals/"+500).contentType(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @Order(7)
    void getRentalInfo() throws Exception {
        mvc.perform(get("/api/rental/"+rentalID).contentType(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @Order(8)
    void changeStatus() throws Exception {
        Long statusID = rentalStatusRepository.findByName(RentalStatusEnum.STATUS_PENDING).getId();

        mvc.perform(put("/api/rental/"+rentalID+"/status/"+statusID)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @Order(9)
    void changeRentalInformation() throws Exception{
        EditCarRentalRequest carRentalRequest = new EditCarRentalRequest();
        carRentalRequest.setStartDate(LocalDate.now().plusDays(1));
        carRentalRequest.setEndDate(LocalDate.now().plusDays(5));

        mvc.perform(put("/api/rental/"+rentalID).contentType(APPLICATION_JSON_VALUE)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                        .content(
                        new ObjectMapper()
                                .registerModule(new JavaTimeModule())
                                .writeValueAsString(carRentalRequest)
                        )
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test()
    @Order(10)
    void deleteAll() throws Exception {
        List<Rental> rentalList = new ArrayList<>(rentalRepository.findByCarId(carID));
        for(Rental rental : rentalList){
            mvc.perform(delete("/api/rental/"+rental.getId())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                    .andExpect(status().isOk())
                    .andReturn();
        }

        rentalList = new ArrayList<>(rentalRepository.findByUserId(userID));
        for(Rental rental : rentalList){
            mvc.perform(delete("/api/rental/"+rental.getId())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                    .andExpect(status().isOk())
                    .andReturn();
        }

        mvc.perform(delete("/api/car/"+carID)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andExpect(status().isOk())
                .andReturn();

        userRepository.deleteById(userID);
    }
}