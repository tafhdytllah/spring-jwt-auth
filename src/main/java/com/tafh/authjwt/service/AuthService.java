package com.tafh.authjwt.service;

import com.tafh.authjwt.entity.User;
import com.tafh.authjwt.model.UserLoginRequest;
import com.tafh.authjwt.model.UserRegisterRequest;
import com.tafh.authjwt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public User register(UserRegisterRequest request) {

        validationService.validate(request);

//        if (userRepository.existByUsername(request.getUsername())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
//        }
//
//        if (userRepository.existByEmail(request.getEmail())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exist");
//        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return userRepository.save(user);

    }

    @Transactional
    public User authenticate(UserLoginRequest request) {
        validationService.validate(request);

        log.error("auth manager start");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        log.error("auth manager end");

        return userRepository.findByUsername(request.getUsername())
                .orElseThrow();
    }

}
