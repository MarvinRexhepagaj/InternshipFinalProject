package com.lhind.internship.FinalProject.repository;

import com.lhind.internship.FinalProject.model.entity.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking,Long> {

    boolean existsByFlightId(Long flightId);

    List<FlightBooking> findByFlightId(Long flightId);
}
