package org.zalmoxis.evetic.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zalmoxis.evetic.config.JwtUtil;
import org.zalmoxis.evetic.dtos.auth.response.LoginResDto;
import org.zalmoxis.evetic.dtos.auth.request.RegisterReqDto;
import org.zalmoxis.evetic.dtos.auth.response.RegisterResDto;
import org.zalmoxis.evetic.entities.User;
import org.zalmoxis.evetic.entities.UserRole;
import org.zalmoxis.evetic.exceptions.UserException;
import org.zalmoxis.evetic.exceptions.UserNotFoundException;
import org.zalmoxis.evetic.mappers.AuthMapper;
import org.zalmoxis.evetic.repositories.EventRepo;
import org.zalmoxis.evetic.repositories.UserRepo;
import org.zalmoxis.evetic.services.UserService;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl
        implements UserService
{

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;
    private final JwtUtil jwtUtil;
    private final EventRepo eventRepo;

    @Override
    public UUID getUserIdByUsername(String username)
    {
        return userRepo.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public RegisterResDto register(RegisterReqDto registerReqDto) {

        if(userRepo.existsByUsername(registerReqDto.getUsername())) {
            throw new UserException("Username is already taken");
        }
        if(userRepo.existsByEmail(registerReqDto.getEmail())) {
            throw new UserException("Email is already in use");
        }

        User user = User.builder()
                .username(registerReqDto.getUsername())
                .email(registerReqDto.getEmail())
                .password(passwordEncoder.encode(registerReqDto.getPassword()))
                .roles(Set.of(UserRole.ATTENDEE))
                .build();

        User savedUser = userRepo.save(user);

        return authMapper.toDto(savedUser);
    }

    @Override
    public LoginResDto login(String username)
    {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());

        return new LoginResDto(token);
    }


}
