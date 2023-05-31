package com.lhind.internship.FinalProject.service.impl;

import com.lhind.internship.FinalProject.exception.CustomException;
import com.lhind.internship.FinalProject.exception.DuplicateException;
import com.lhind.internship.FinalProject.mapper.FlightMapper;
import com.lhind.internship.FinalProject.model.dto.FlightDto;
import com.lhind.internship.FinalProject.model.dto.FlightSearchCriteria;
import com.lhind.internship.FinalProject.model.entity.Flight;
import com.lhind.internship.FinalProject.model.entity.FlightBooking;
import com.lhind.internship.FinalProject.repository.BookingRepository;
import com.lhind.internship.FinalProject.repository.FlightBookingRepository;
import com.lhind.internship.FinalProject.repository.FlightRepository;
import com.lhind.internship.FinalProject.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final FlightBookingRepository flightBookingRepository;

    private final BookingRepository bookingRepository;

    private final FlightMapper flightMapper;

    public FlightServiceImpl(FlightRepository flightRepository, FlightBookingRepository flightBookingRepository, BookingRepository bookingRepository, FlightMapper flightMapper) {
        this.flightRepository = flightRepository;
        this.flightBookingRepository = flightBookingRepository;
        this.bookingRepository = bookingRepository;
        this.flightMapper = flightMapper;
    }

    @Override
    public FlightDto createFlight(FlightDto flightDto) {
        try {
            Flight flight = flightMapper.toEntity(flightDto);
            flight = flightRepository.save(flight);
            return flightMapper.toDto(flight);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateException("Flight already exists with the same flight number and flight date");
        }
    }
    @Override
    public FlightDto getFlightById(Long id) {
        log.info("Getting flight by ID: {}", id);

        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Flight not found with ID: {}", id);
                    return new CustomException("Flight not found with ID: " + id);
                });

        log.info("Retrieved flight with ID: {}", id);

        return flightMapper.toDto(flight);
    }

    @Override
    public List<FlightDto> searchFlights(FlightSearchCriteria searchCriteria) {
        List<Flight> flights = flightRepository.searchFlights(
                searchCriteria.getOrigin(),
                searchCriteria.getDestination(),
                searchCriteria.getFlightDate(),
                searchCriteria.getAirlineCode()
        );
        return flightMapper.toDtoList(flights);
    }


    @Override
    public FlightDto updateFlight(Long id, FlightDto flightDto) {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new CustomException("Flight not found with id: " + id));

        boolean bookingsExist = flightBookingRepository.existsByFlightId(id);

        if (bookingsExist && flightDto.getDepartureTime().equals(existingFlight.getDepartureTime())) {
            throw new DuplicateException("Flight is booked. Only the departure time can be changed.");
        }

        existingFlight.setDepartureTime(flightDto.getDepartureTime());
        // Set any other fields you want to update

        flightRepository.save(existingFlight);
        return flightMapper.toDto(existingFlight);
    }
    @Override
    public void deleteFlight(Long id) throws CustomException {
        Flight existingFlight = flightRepository.findById(id)
                .orElseThrow(() -> new CustomException("Flight not found with id: " + id));

        List<FlightBooking> flightBookings = flightBookingRepository.findByFlightId(id);

        if (flightBookings.isEmpty()) {
            flightRepository.delete(existingFlight);
        } else {
            throw new CustomException("Flight cannot be deleted as it has been booked.");
        }
    }

    @Override
    public List<FlightDto> getFlightsByBookingIdAndEmail(Long bookingId, String email) {
        log.info("Getting flights by booking ID: {} and email: {}", bookingId, email);

        List<Flight> flights = flightRepository.findByBookingIdAndEmail(bookingId, email);

        log.info("Retrieved {} flights for booking ID: {} and email: {}", flights.size(), bookingId, email);

        return flightMapper.toDtoList(flights);
    }
}
