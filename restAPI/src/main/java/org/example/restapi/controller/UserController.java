package org.example.restapi.controller;

import org.example.restapi.model.UpdateUserModel;
import org.example.restapi.model.UserModel;
import org.example.restapi.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public List<UserModel> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public UserModel getUser(@PathVariable String username) {
        return userService.getUser(username);
    }

    @PutMapping()
    @PreAuthorize("isAuthenticated()")
    public UserModel updateUser(Authentication authentication, @RequestBody UpdateUserModel updateUserModel) {
        return userService.updateUser(authentication.getName(), updateUserModel.getMainPage());
    }
}
