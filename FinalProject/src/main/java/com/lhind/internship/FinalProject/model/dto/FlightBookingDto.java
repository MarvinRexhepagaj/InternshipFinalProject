package com.lhind.internship.FinalProject.model.dto;

import com.lhind.internship.FinalProject.model.enums.BookingClass;
import com.lhind.internship.FinalProject.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightBookingDto {
    private Long id;
    private Long bookingId;
    private Long flightId;
    private BookingClass bookingClass;
    private Integer seatsBooked;
    private BookingStatus bookingStatus;
    private String declineReason;
    private Date bookingDate;


}
