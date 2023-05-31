package com.lhind.internship.FinalProject.controller;

import com.lhind.internship.FinalProject.exception.CustomException;
import com.lhind.internship.FinalProject.model.dto.AuthenticationRequest;
import com.lhind.internship.FinalProject.model.dto.AuthenticationResponse;
import com.lhind.internship.FinalProject.service.UserService;
import com.lhind.internship.FinalProject.service.impl.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;
    private final UserService userService;

    public AuthenticationController(AuthenticationServiceImpl authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST,path = "/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        try {
            return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
        }catch (CustomException e){
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}