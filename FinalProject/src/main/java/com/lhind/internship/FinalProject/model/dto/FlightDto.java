package com.lhind.internship.FinalProject.model.dto;

import com.lhind.internship.FinalProject.model.enums.AirlineCode;
import com.lhind.internship.FinalProject.model.enums.FlightStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {

    private Long id;
    private AirlineCode airlineCode;
    private String flightNumber;
    private String origin;
    private String destination;
    private Date flightDate;
    private Date departureTime;
    private String aircraftType;
    private Integer economySeats;
    private Integer premiumEconomySeats;
    private Integer businessSeats;
    private Integer firstSeats;
    private FlightStatus flightStatus;
    private Double price;

}