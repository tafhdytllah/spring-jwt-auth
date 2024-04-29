package com.tafh.authjwt.controller;

import com.tafh.authjwt.entity.User;
import com.tafh.authjwt.model.*;
import com.tafh.authjwt.service.AuthService;
import com.tafh.authjwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<User> register(@RequestBody UserRegisterRequest request) {
        User registeredUser = authService.register(request);

        return WebResponse.<User>builder()
                .data(registeredUser)
                .build();
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        User authenticatedUser = authService.authenticate(request);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        UserLoginResponse loginResponse = new UserLoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiredAt(jwtService.getExpirationTime());

        return WebResponse.<UserLoginResponse>builder()
                .data(loginResponse)
                .build();
    }
}
