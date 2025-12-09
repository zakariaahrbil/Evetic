package org.zalmoxis.evetic.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zalmoxis.evetic.dtos.RegisterReqDto;
import org.zalmoxis.evetic.dtos.RegisterResDto;
import org.zalmoxis.evetic.entities.User;
import org.zalmoxis.evetic.entities.UserRole;
import org.zalmoxis.evetic.exceptions.UserNotFoundException;
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

    @Override
    public UUID getUserIdByUsername(String username)
    {
        return userRepo.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public RegisterResDto register(RegisterReqDto registerReqDto) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(registerReqDto.getUsername())
                .email(registerReqDto.getEmail())
                .password(passwordEncoder.encode(registerReqDto.getPassword()))
                .roles(Set.of(UserRole.ATTENDEE))
                .build();

        User savedUser = userRepo.save(user);

        return RegisterResDto.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .email(savedUser.getEmail())
                .build();
    }
}
