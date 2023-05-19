package com.lhind.internship.FinalProject.model.dto;

import com.lhind.internship.FinalProject.model.entity.CancellationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BookingDto {

    private Long id;
    private Long userId;
    private Date bookingDate;
    private List<FlightBookingDto> flightBookings;
    private CancellationRequest cancellationRequest;


}