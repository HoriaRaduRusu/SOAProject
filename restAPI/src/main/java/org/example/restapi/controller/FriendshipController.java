package org.example.restapi.controller;

import org.example.restapi.model.FriendshipModel;
import org.example.restapi.service.FriendshipService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping("/{username}")
    public List<FriendshipModel> getUserFriends(@PathVariable String username) {
        return friendshipService.getUserFriends(username);
    }

    @PostMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public void createFriendship(Authentication authentication, @PathVariable String username) {
        friendshipService.createFriendship(username, authentication.getName());
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public void deleteFriendship(Authentication authentication, @PathVariable String username) {
        friendshipService.deleteFriendship(username, authentication.getName());
    }
}
