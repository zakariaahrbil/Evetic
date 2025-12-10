package org.zalmoxis.evetic.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalmoxis.evetic.dtos.EventCreationReqDto;
import org.zalmoxis.evetic.dtos.EventCreationResDto;
import org.zalmoxis.evetic.mappers.EventMapper;
import org.zalmoxis.evetic.services.EventService;
import org.zalmoxis.evetic.services.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController
{
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<EventCreationResDto> createEvent(
            @Valid @RequestBody EventCreationReqDto eventCreationReqDto,
            Authentication authentication
    )
    {
        String username = authentication.getName();
        UUID userId = userService.getUserIdByUsername(username);

        EventCreationResDto eventCreationResDto = eventMapper.toDto(
                eventService.createEvent(userId, eventCreationReqDto)
        );

        return new ResponseEntity<>(eventCreationResDto, HttpStatus.CREATED);
    }
}