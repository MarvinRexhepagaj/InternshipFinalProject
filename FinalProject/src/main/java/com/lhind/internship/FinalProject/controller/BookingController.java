package com.lhind.internship.FinalProject.controller;

import com.lhind.internship.FinalProject.exception.CustomException;
import com.lhind.internship.FinalProject.mapper.BookingMapper;
import com.lhind.internship.FinalProject.model.dto.BookingDto;
import com.lhind.internship.FinalProject.service.BookingService;
import com.lhind.internship.FinalProject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final UserService userService;

    private final BookingService bookingService;

    private final BookingMapper bookingMapper;

    public BookingController(UserService userService, BookingService bookingService, BookingMapper bookingMapper) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

  /*  @PostMapping("user/{userId}")
    public ResponseEntity<BookingDto> createBooking(@PathVariable Long userId, @RequestBody @Valid BookingDto bookingDto) throws UserNotFoundException {
        BookingDto createdBookingDto = bookingService.createBooking(userId, bookingDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBookingDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdBookingDto);

    }*/
    @PreAuthorize(value = "hasAnyRole('TRAVELLER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDto>> getBookingsByUserId(@PathVariable Long userId)throws CustomException {
        List<BookingDto> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }
    @PreAuthorize(value = "hasAnyRole('TRAVELLER')")
    @GetMapping("/traveler")
    public ResponseEntity<List<BookingDto>> getBookingsForAuthenticatedTraveler(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize,
            Authentication authentication
    ) {
        String userEmail = authentication.getName();
        List<BookingDto> bookings = bookingService.getBookingsForAuthenticatedTraveler(userEmail, page, pageSize);
        return ResponseEntity.ok(bookings);
    }

}
