package com.example.sfmproject.Controllers;

import com.example.sfmproject.DTO.SignIn;
import com.example.sfmproject.Entities.User;
import com.example.sfmproject.JWT.JwtAuthTokenFilter;
import com.example.sfmproject.JWT.JwtProvider;
import com.example.sfmproject.JWT.JwtResponse;
import com.example.sfmproject.JWT.NewTokensResponses;
import com.example.sfmproject.Repositories.RoleRepository;
import com.example.sfmproject.Repositories.UserRepository;
import com.example.sfmproject.ServiceImpl.MailSenderService;
import com.example.sfmproject.ServiceImpl.UserServiceIMP;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserServiceIMP userServiceIMP;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    MailSenderService mailSending;
    @Autowired
    JwtAuthTokenFilter jwtAuthTokenFilter;
    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody SignIn login, HttpServletRequest request) {
        Optional<User> userByEmail = userRepository.findByEmail(login.getEmail());
        Optional<User> userByUsername = userRepository.findByUsername(login.getEmail());
        Optional<User> user = userByEmail.isPresent() ? userByEmail : userByUsername;

        if (user.isPresent() && user.get().isBlocked() && user.get().isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Send email upon successful login
        String subject = "CIAOOOO BELLOOO ----> Login Successful";
        String body = "Welcome back, " + user.get().getName() + "! You have logged in successfully.";
        try {
            mailSending.send(user.get().getEmail(), subject, body);
        } catch (MessagingException e) {
            // Handle email sending exception
            e.printStackTrace();
        }

        // Si tu n'as pas de token GitHub ici, passe une chaîne vide
        List<String> jwt = jwtProvider.generateJwtTokens(authentication, "");

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt.get(0), jwt.get(1), userDetails.getUsername(), user.get().getId(), userDetails.getAuthorities()));
    }

    /* @PostMapping("/signIn")

    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody SignIn login) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));

    }*/
    // Create a new controller method for token refresh

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken( HttpServletRequest request) {
        String refreshToken = jwtAuthTokenFilter.extractRefreshToken(request);
        if (refreshToken != null && jwtAuthTokenFilter.isValidRefreshToken(refreshToken)) {
            String newAccessToken = jwtAuthTokenFilter.issueNewAccessToken(refreshToken);
            // String newRefreshToken = jwtProvider.generateRefreshToken();
            return ResponseEntity.ok(new NewTokensResponses(refreshToken, newAccessToken));
        } else {
            return ResponseEntity.badRequest().body("expired refresh token");
        }
    }


    @RequestMapping(value = "/signup/employee/{roleName}", method = RequestMethod.POST)
    public ResponseEntity<User> registerUser(@Validated @RequestBody User user1,@PathVariable ("roleName")String roleName) {
        return userServiceIMP.registerUser(user1,  roleName);
    }

    @RequestMapping(value = "/signup/entreprise", method = RequestMethod.POST)
    public ResponseEntity<User> registerEntreprise(@Validated @RequestBody User user1){
        return userServiceIMP.registerEntreprise(user1);
    }
    @RequestMapping(value = "/signupadmin", method = RequestMethod.POST)
    public ResponseEntity<User> registerAdmin(@Valid @RequestBody User user)  {
        return userServiceIMP.registerAdmin(user);
    }


//    @PostMapping("/emailsignIn")
//    public ResponseEntity<JwtResponse> authenticateUser2(@RequestBody SignIn login, HttpServletRequest request) {
//        Optional<User> userByEmail = userRepository.findByEmail(login.getEmail());
//        Optional<User> userByUsername = userRepository.findByUsername(login.getEmail());
//        Optional<User> user = userByEmail.isPresent() ? userByEmail : userByUsername;
//
//        if (user.isPresent() && user.get().isBlocked() && user.get().isValid()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Send email upon successful login
//        String subject = "Login Successful";
//        String body = "Welcome back, " + user.get().getName() + "! You have logged in successfully.";
//        try {
//            mailSending.send(user.get().getEmail(), subject, body);
//        } catch (MessagingException e) {
//            // Handle email sending exception
//            e.printStackTrace();
//        }
//
//        List<String> jwt = jwtProvider.generateJwtTokens(authentication);
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return ResponseEntity.ok(new JwtResponse(jwt.get(0), jwt.get(1), userDetails.getUsername(), user.get().getId(), userDetails.getAuthorities()));
//    }
}
