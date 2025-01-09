package org.example.restapi.controller;

import org.example.restapi.model.FollowModel;
import org.example.restapi.service.FollowersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/followers")
public class FollowersController {
    private final FollowersService followersService;

    public FollowersController(FollowersService followersService) {
        this.followersService = followersService;
    }

    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public List<FollowModel> getUserFollowers(@PathVariable String username) {
        return followersService.getUserFollowers(username);
    }

    @GetMapping("/{username}/followed")
    @PreAuthorize("isAuthenticated()")
    public List<FollowModel> getUserFollowed(@PathVariable String username) {
        return followersService.getUserFollowed(username);
    }

    @PostMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public void follow(Authentication authentication, @PathVariable String username) {
        followersService.follow(username, authentication.getName());
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public void unfollow(Authentication authentication, @PathVariable String username) {
        followersService.unfollow(username, authentication.getName());
    }
}
