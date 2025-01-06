package org.example.authenticationserver.controller;

import org.example.authenticationserver.model.AuthenticateUserDTO;
import org.example.authenticationserver.model.JwkSetResponseDTO;
import org.example.authenticationserver.model.JwtResponseDTO;
import org.example.authenticationserver.model.RegisterUserDTO;
import org.example.authenticationserver.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/jwks")
    public JwkSetResponseDTO getJwks() {
        return userService.getJwks();
    }

    @PostMapping("/authenticate")
    public JwtResponseDTO authenticate(@RequestBody AuthenticateUserDTO authenticateUserDTO) {
        return userService.authenticate(authenticateUserDTO.getEmail(), authenticateUserDTO.getPassword());
    }

    @PostMapping("/register")
    public JwtResponseDTO register(@RequestBody RegisterUserDTO registerUserDTO) {
        return userService.register(registerUserDTO);
    }
}
