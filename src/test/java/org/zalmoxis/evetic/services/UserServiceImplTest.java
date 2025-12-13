package org.zalmoxis.evetic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zalmoxis.evetic.config.JwtUtil;
import org.zalmoxis.evetic.dtos.auth.request.RegisterReqDto;
import org.zalmoxis.evetic.dtos.auth.response.LoginResDto;
import org.zalmoxis.evetic.dtos.auth.response.RegisterResDto;
import org.zalmoxis.evetic.entities.User;
import org.zalmoxis.evetic.entities.UserRole;
import org.zalmoxis.evetic.exceptions.UserException;
import org.zalmoxis.evetic.exceptions.UserNotFoundException;
import org.zalmoxis.evetic.mappers.AuthMapper;
import org.zalmoxis.evetic.repositories.EventRepo;
import org.zalmoxis.evetic.repositories.UserRepo;
import org.zalmoxis.evetic.services.implementations.UserServiceImpl;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthMapper authMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private EventRepo eventRepo;

    @InjectMocks
    private UserServiceImpl userService;

    private UUID userId;
    private User user;
    private RegisterReqDto registerReqDto;
    private RegisterResDto registerResDto;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .username("testuser")
                .email("test@test.com")
                .password("encodedPassword")
                .roles(Set.of(UserRole.ATTENDEE))
                .build();

        registerReqDto = RegisterReqDto.builder()
                .username("newuser")
                .email("newuser@test.com")
                .password("password123")
                .build();

        registerResDto = new RegisterResDto();
    }

    @Test
    void getUserIdByUsername_ShouldReturnUserId_WhenUserExists() {
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));

        UUID result = userService.getUserIdByUsername("testuser");

        assertNotNull(result);
        assertEquals(userId, result);
        verify(userRepo, times(1)).findByUsername("testuser");
    }

    @Test
    void getUserIdByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserIdByUsername("unknown"));
        verify(userRepo, times(1)).findByUsername("unknown");
    }

    @Test
    void register_ShouldCreateUser_WhenValidData() {
        when(userRepo.existsByUsername("newuser")).thenReturn(false);
        when(userRepo.existsByEmail("newuser@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(UUID.randomUUID());
            return u;
        });
        when(authMapper.toDto(any(User.class))).thenReturn(registerResDto);

        RegisterResDto result = userService.register(registerReqDto);

        assertNotNull(result);
        verify(userRepo, times(1)).existsByUsername("newuser");
        verify(userRepo, times(1)).existsByEmail("newuser@test.com");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {
        when(userRepo.existsByUsername("newuser")).thenReturn(true);

        assertThrows(UserException.class, () -> userService.register(registerReqDto));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        when(userRepo.existsByUsername("newuser")).thenReturn(false);
        when(userRepo.existsByEmail("newuser@test.com")).thenReturn(true);

        assertThrows(UserException.class, () -> userService.register(registerReqDto));
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void login_ShouldReturnToken_WhenUserExists() {
        String expectedToken = "jwt-token-123";
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("testuser", user.getRoles())).thenReturn(expectedToken);

        LoginResDto result = userService.login("testuser");

        assertNotNull(result);
        assertEquals(expectedToken, result.getToken());
        verify(userRepo, times(1)).findByUsername("testuser");
        verify(jwtUtil, times(1)).generateToken("testuser", user.getRoles());
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        when(userRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login("unknown"));
        verify(jwtUtil, never()).generateToken(any(), any());
    }
}

