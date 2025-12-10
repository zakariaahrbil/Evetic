package org.zalmoxis.evetic.controllers;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalmoxis.evetic.dtos.EventCreationReqDto;
import org.zalmoxis.evetic.dtos.EventCreationResDto;
import org.zalmoxis.evetic.dtos.EventDetailsResDto;
import org.zalmoxis.evetic.dtos.EventResDto;
import org.zalmoxis.evetic.mappers.EventMapper;
import org.zalmoxis.evetic.services.EventService;
import org.zalmoxis.evetic.utils.UUIDFromAuthentication;


@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController    
{
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final UUIDFromAuthentication uuidFromAuthentication;

    @PostMapping
    public ResponseEntity<EventCreationResDto> createEvent(
            @Valid @RequestBody EventCreationReqDto eventCreationReqDto,
            Authentication authentication
    )
    {

        EventCreationResDto eventCreationResDto = eventMapper.toDto(
                eventService.createEvent(uuidFromAuthentication.getUUIDFromAuthentication(authentication),
                        eventCreationReqDto)
        );

        return new ResponseEntity<>(eventCreationResDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EventResDto>> getAllEventsForOrganizer(Authentication authentication, Pageable pageable){

        Page<EventResDto> events = eventService.getEventsForOrganizer(
                uuidFromAuthentication.getUUIDFromAuthentication(authentication),
                        pageable)
                .map(eventMapper::toEventResDto);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<EventResDto>> getAllEvents(Pageable pageable){

        Page<EventResDto> events = eventService.getAllEvents(
                        pageable)
                .map(eventMapper::toEventResDto);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDetailsResDto> getEventDetailsByIdAndOrganizer(@PathVariable("eventId") String eventId, Authentication authentication){

        EventDetailsResDto eventDetailsResDto = eventMapper.toEventDetailsResDto(
                eventService.getEventByIdAndOrganizer(
                        java.util.UUID.fromString(eventId),
                        uuidFromAuthentication.getUUIDFromAuthentication(authentication)
                )
        );

        return new ResponseEntity<>(eventDetailsResDto, HttpStatus.OK);

    }


}