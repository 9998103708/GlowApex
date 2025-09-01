package com.glowapex.controller;

import com.glowapex.dto.PasswordChangeRequest;
import com.glowapex.dto.UserProfileDTO;
import com.glowapex.dto.UserProfileUpdateRequest;
import com.glowapex.entity.User;
import com.glowapex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserProfileDTO getProfile(@AuthenticationPrincipal User user) {
        return userService.getProfile(user);
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal User user,
                                 @RequestBody PasswordChangeRequest request) {
        return userService.changePassword(user, request);
    }

    @GetMapping("/stats")
    public Map<String, Object> getUserStats(@AuthenticationPrincipal User user) {
        return userService.getUserStats(user);
    }

    @PutMapping("/{id}/profile")
    public UserProfileDTO updateProfile(@PathVariable Long id,
                                        @RequestBody UserProfileUpdateRequest request) {
        return userService.updateProfile(id, request);
    }
}