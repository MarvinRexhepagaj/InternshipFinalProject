package com.lhind.internship.FinalProject.controller;

import com.lhind.internship.FinalProject.exception.CustomException;
import com.lhind.internship.FinalProject.model.dto.BaseResponse;
import com.lhind.internship.FinalProject.model.dto.FlightDto;
import com.lhind.internship.FinalProject.model.dto.FlightSearchCriteria;
import com.lhind.internship.FinalProject.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDto> getFlightById(@PathVariable("id") Long id) throws CustomException {
        FlightDto flightDto = flightService.getFlightById(id);
        return ResponseEntity.ok(flightDto);
    }
    @GetMapping("/search")
    public ResponseEntity<List<FlightDto>> searchFlights(FlightSearchCriteria searchCriteria) {
        List<FlightDto> flights = flightService.searchFlights(searchCriteria);
        return ResponseEntity.ok(flights);
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<FlightDto> createFlight(@RequestBody @Valid FlightDto flightDto) {
        return ResponseEntity.ok(flightService.createFlight(flightDto));
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<FlightDto> updateFlight(@PathVariable Long id, @RequestBody @Valid FlightDto flightDto) {
        FlightDto updatedFlight = flightService.updateFlight(id, flightDto);
        return ResponseEntity.ok(updatedFlight);
    }
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) throws CustomException {
        flightService.deleteFlight(id);
        return ResponseEntity.ok("Flight deleted successfully");
    }

}