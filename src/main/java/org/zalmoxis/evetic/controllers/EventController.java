package org.zalmoxis.evetic.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalmoxis.evetic.dtos.EventCreationReqDto;
import org.zalmoxis.evetic.dtos.EventCreationResDto;
import org.zalmoxis.evetic.mappers.EventMapper;
import org.zalmoxis.evetic.requests.EventCreationReq;
import org.zalmoxis.evetic.services.EventService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController
{
    private final EventService eventService;
    private final EventMapper eventMapper;

    @PostMapping
    public ResponseEntity<EventCreationResDto> createEvent(
           @Valid @RequestBody EventCreationReqDto eventCreationReqDto,
            @AuthenticationPrincipal Jwt jwt
    )
    {
        EventCreationReq eventCreationReq = eventMapper.fromDto(eventCreationReqDto);
        UUID uuid = UUID.fromString(jwt.getSubject());

        EventCreationResDto eventCreationResDto = eventMapper.toDto(
                eventService.createEvent(uuid, eventCreationReq)
        );


        return new ResponseEntity<>(eventCreationResDto, HttpStatus.CREATED);
    }
}
