package org.zalmoxis.evetic.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.zalmoxis.evetic.services.UserService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UUIDFromAuthentication {
    private final UserService userService;

    public UUID getUUIDFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return userService.getUserIdByUsername(username);
    }
}
