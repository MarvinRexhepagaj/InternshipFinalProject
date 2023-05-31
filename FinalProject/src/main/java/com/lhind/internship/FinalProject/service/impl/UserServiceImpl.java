package com.lhind.internship.FinalProject.service.impl;


import com.lhind.internship.FinalProject.exception.CustomException;
import com.lhind.internship.FinalProject.mapper.UserMapper;
import com.lhind.internship.FinalProject.model.dto.BookingDto;
import com.lhind.internship.FinalProject.model.dto.UserDto;
import com.lhind.internship.FinalProject.model.entity.FlightBooking;
import com.lhind.internship.FinalProject.model.entity.User;
import com.lhind.internship.FinalProject.repository.BookingRepository;
import com.lhind.internship.FinalProject.repository.FlightBookingRepository;
import com.lhind.internship.FinalProject.repository.UserRepository;
import com.lhind.internship.FinalProject.service.BookingService;
import com.lhind.internship.FinalProject.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private final FlightBookingRepository flightBookingRepository;

    private final BCryptPasswordEncoder passwordEncoder;


    private final UserMapper userMapper;


    public UserServiceImpl(UserRepository userRepository, BookingRepository bookingRepository, FlightBookingRepository flightBookingRepository, BCryptPasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.flightBookingRepository = flightBookingRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }



    @Override
    public UserDto createUser(UserDto userDto) {


        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPassword);

        User user = userMapper.toEntity(userDto);
        User createdUser = userRepository.save(user);
        return userMapper.toDto(createdUser);
    }


    @Override
    public List<UserDto> getAllUsers()  {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new CustomException("No users found");
        }

        return users.stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new CustomException("User not found with ID: " + id);
        }

        return userMapper.toDto(user.get());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow(() -> new CustomException("User not found with email: " + email));
        return userMapper.toDto(user);
}
    @Override
    public List<UserDto> getUsersByFlightId(Long flightId) {
        List<FlightBooking> flightBookings = flightBookingRepository.findByFlightId(flightId);

        if (flightBookings.isEmpty()) {

            throw new CustomException("No flight bookings found for flightId: " + flightId);
        }

        List<User> users = flightBookings.stream()
                .map(fb -> fb.getBooking().getUser())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return userMapper.toDtoList(users);
    }


    @Override
    public UserDto updateUser(Long id, UserDto userDto)  {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with ID: " + id));

        User updatedUser = userMapper.toEntity(userDto);
        updatedUser.setId(existingUser.getId()); // Ensure the id is not changed
        userRepository.save(updatedUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) throws CustomException {
        if (!userRepository.existsById(id)) {
             new CustomException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }


}
