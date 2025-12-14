package org.zalmoxis.evetic.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.zalmoxis.evetic.services.UserService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UUIDFromAuthenticationTest {

    @Mock
    private UserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UUIDFromAuthentication uuidFromAuthentication;

    private UUID userId;
    private String username;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        username = "testuser";
    }

    @Test
    void getUUIDFromAuthentication_ShouldReturnUserId() {
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserIdByUsername(username)).thenReturn(userId);

        UUID result = uuidFromAuthentication.getUUIDFromAuthentication(authentication);

        assertNotNull(result);
        assertEquals(userId, result);
        verify(authentication, times(1)).getName();
        verify(userService, times(1)).getUserIdByUsername(username);
    }

    @Test
    void getUUIDFromAuthentication_ShouldCallAuthenticationGetName() {
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserIdByUsername(username)).thenReturn(userId);

        uuidFromAuthentication.getUUIDFromAuthentication(authentication);

        verify(authentication).getName();
    }

    @Test
    void getUUIDFromAuthentication_ShouldCallUserServiceWithCorrectUsername() {
        String specificUsername = "specificUser";
        UUID specificUserId = UUID.randomUUID();

        when(authentication.getName()).thenReturn(specificUsername);
        when(userService.getUserIdByUsername(specificUsername)).thenReturn(specificUserId);

        UUID result = uuidFromAuthentication.getUUIDFromAuthentication(authentication);

        assertEquals(specificUserId, result);
        verify(userService).getUserIdByUsername(specificUsername);
    }

    @Test
    void getUUIDFromAuthentication_ShouldReturnDifferentIdsForDifferentUsers() {
        UUID user1Id = UUID.randomUUID();
        UUID user2Id = UUID.randomUUID();

        when(authentication.getName()).thenReturn("user1");
        when(userService.getUserIdByUsername("user1")).thenReturn(user1Id);

        UUID result1 = uuidFromAuthentication.getUUIDFromAuthentication(authentication);

        when(authentication.getName()).thenReturn("user2");
        when(userService.getUserIdByUsername("user2")).thenReturn(user2Id);

        UUID result2 = uuidFromAuthentication.getUUIDFromAuthentication(authentication);

        assertNotEquals(result1, result2);
    }
}

