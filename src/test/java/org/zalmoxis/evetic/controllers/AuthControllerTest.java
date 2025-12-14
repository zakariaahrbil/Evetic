package org.zalmoxis.evetic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.zalmoxis.evetic.dtos.auth.request.LoginReqDto;
import org.zalmoxis.evetic.dtos.auth.request.RegisterReqDto;
import org.zalmoxis.evetic.dtos.auth.response.LoginResDto;
import org.zalmoxis.evetic.dtos.auth.response.RegisterResDto;
import org.zalmoxis.evetic.services.UserService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private RegisterReqDto registerReqDto;
    private LoginReqDto loginReqDto;
    private RegisterResDto registerResDto;
    private LoginResDto loginResDto;

    @BeforeEach
    void setUp() {
        registerReqDto = RegisterReqDto.builder()
                .username("testuser")
                .email("test@test.com")
                .password("password123")
                .build();

        registerResDto = RegisterResDto.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("test@test.com")
                .build();

        loginReqDto = new LoginReqDto();
        loginReqDto.setUsername("testuser");
        loginReqDto.setPassword("password123");

        loginResDto = LoginResDto.builder()
                .token("jwt-token-123")
                .build();
    }

    @Test
    void register_ShouldReturnCreatedUser() {
        when(userService.register(any(RegisterReqDto.class))).thenReturn(registerResDto);

        ResponseEntity<RegisterResDto> response = authController.register(registerReqDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals("test@test.com", response.getBody().getEmail());
        verify(userService, times(1)).register(any(RegisterReqDto.class));
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsValid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userService.login("testuser")).thenReturn(loginResDto);

        ResponseEntity<LoginResDto> response = authController.login(loginReqDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token-123", response.getBody().getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, times(1)).login("testuser");
    }

    @Test
    void login_ShouldCallAuthenticationManager_WithCorrectCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userService.login("testuser")).thenReturn(loginResDto);

        authController.login(loginReqDto);

        verify(authenticationManager).authenticate(argThat(token ->
            token.getPrincipal().equals("testuser") &&
            token.getCredentials().equals("password123")
        ));
    }

    @Test
    void login_ShouldThrowException_WhenCredentialsInvalid() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authController.login(loginReqDto));
        verify(userService, never()).login(anyString());
    }

    @Test
    void register_ShouldPassCorrectDataToService() {
        when(userService.register(any(RegisterReqDto.class))).thenReturn(registerResDto);

        authController.register(registerReqDto);

        verify(userService).register(argThat(dto ->
            dto.getUsername().equals("testuser") &&
            dto.getEmail().equals("test@test.com") &&
            dto.getPassword().equals("password123")
        ));
    }
}

