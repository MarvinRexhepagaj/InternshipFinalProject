package com.lhind.internship.FinalProject.service.impl;

import com.lhind.internship.FinalProject.exception.CustomException;
import com.lhind.internship.FinalProject.exception.DuplicateException;
import com.lhind.internship.FinalProject.mapper.BookingMapper;
import com.lhind.internship.FinalProject.model.dto.BookingDto;
import com.lhind.internship.FinalProject.model.dto.FlightBookingDto;
import com.lhind.internship.FinalProject.model.dto.FlightDto;
import com.lhind.internship.FinalProject.model.entity.Booking;
import com.lhind.internship.FinalProject.model.entity.User;
import com.lhind.internship.FinalProject.model.enums.BookingClass;
import com.lhind.internship.FinalProject.repository.BookingRepository;
import com.lhind.internship.FinalProject.repository.CancellationRequestRepository;
import com.lhind.internship.FinalProject.repository.FlightBookingRepository;
import com.lhind.internship.FinalProject.repository.UserRepository;
import com.lhind.internship.FinalProject.service.BookingService;
import com.lhind.internship.FinalProject.service.FlightBookingService;
import com.lhind.internship.FinalProject.service.FlightService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final CancellationRequestRepository cancellationRequestRepository;

    private final FlightBookingRepository flightBookingRepository;

    private final UserRepository userRepository;
    private final FlightService flightService;
    private final FlightBookingService flightBookingService;

    private final BookingMapper bookingMapper;


    public BookingServiceImpl(BookingRepository bookingRepository, CancellationRequestRepository cancellationRequestRepository, FlightBookingRepository flightBookingRepository, UserRepository userRepository, FlightService flightService, FlightBookingService flightBookingService, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.cancellationRequestRepository = cancellationRequestRepository;
        this.flightBookingRepository = flightBookingRepository;
        this.userRepository = userRepository;
        this.flightService = flightService;
        this.flightBookingService = flightBookingService;
        this.bookingMapper = bookingMapper;
    }





    @Override
    public List<BookingDto> getBookingsForAuthenticatedTraveler(String userEmail, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("bookingDate").descending());
        Page<Booking> bookingsPage = bookingRepository.findByUserEmail(userEmail, pageable);

        if (bookingsPage.isEmpty()) {
            throw new CustomException("You have not booked any flights.");
        }

        return bookingsPage.getContent().stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByUserId(Long userId)throws CustomException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found with ID: " + userId));
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookingMapper.toDtoList(bookings);
    }

    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        // Retrieve the flights for the booking
        List<FlightBookingDto> flightBookings = bookingDto.getFlightBookings();

        // Check if at least one flight is included in the booking
        if (flightBookings.isEmpty()) {
            throw new DuplicateException("At least one flight should be included in the booking.");
        }

        // Check if every flight requested for booking is in the future
        LocalDate currentDate = LocalDate.now();
        for (FlightBookingDto flightBooking : flightBookings) {
            FlightDto flight = flightService.getFlightById(flightBooking.getFlightId());
            LocalDate flightDate = flight.getFlightDate();
            if (flightDate.isBefore(currentDate)) {
                throw new DuplicateException("Flight " + flight.getFlightNumber() + " is not in the future.");
            }
        }

        // Check if every flight requested for booking has enough seats available in the requested booking class
        for (FlightBookingDto flightBooking : flightBookings) {
            FlightDto flight = flightService.getFlightById(flightBooking.getFlightId());
            int seatsBooked = flightBooking.getSeatsBooked();
            BookingClass bookingClass = flightBooking.getBookingClass();
            int availableSeats = getAvailableSeats(flight, bookingClass);
            if (seatsBooked > availableSeats) {
                throw new DuplicateException("Flight " + flight.getFlightNumber() + " does not have enough seats available in the requested booking class.");
            }
        }

        // Save the booking
        Booking booking = bookingMapper.toEntity(bookingDto);
        booking.setBookingDate(LocalDateTime.now());
        Booking savedBooking = bookingRepository.save(booking);
        return bookingMapper.toDto(savedBooking);
    }
    private int getAvailableSeats(FlightDto flight, BookingClass bookingClass) {
        int totalSeats = 0;
        int seatsBooked = 0;

        // Retrieve the total seats for the requested booking class
        switch (bookingClass) {
            case ECONOMY:
                totalSeats = flight.getEconomySeats();
                break;
            case PREMIUM_ECONOMY:
                totalSeats = flight.getPremiumEconomySeats();
                break;
            case BUSINESS:
                totalSeats = flight.getBusinessSeats();
                break;
            case FIRST:
                totalSeats = flight.getFirstSeats();
                break;
        }

        // Retrieve the existing bookings for the flight
        List<FlightBookingDto> existingBookings = flightBookingService.getFlightBookingsByFlightId(flight.getId());

        // Calculate the total seats booked for the requested booking class
        for (FlightBookingDto existingBooking : existingBookings) {
            if (existingBooking.getBookingClass() == bookingClass) {
                seatsBooked += existingBooking.getSeatsBooked();
            }
        }

        // Calculate the available seats
        int availableSeats = totalSeats - seatsBooked;

        return availableSeats;
    }



}
