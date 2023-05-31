package com.lhind.internship.FinalProject.service;

import com.lhind.internship.FinalProject.exception.CustomException;
import com.lhind.internship.FinalProject.model.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto) ;

    List<UserDto> getAllUsers() ;


    UserDto getUserById(Long id) ;

    UserDto getUserByEmail(String email) ;

    List<UserDto> getUsersByFlightId(Long flightId);

    UserDto updateUser(Long id, UserDto userDto) ;

    void deleteUser(Long id) ;


}
