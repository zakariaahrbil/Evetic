package org.zalmoxis.evetic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalmoxis.evetic.dtos.event.response.EventFullResDto;
import org.zalmoxis.evetic.dtos.event.response.EventPublishedResDto;
import org.zalmoxis.evetic.dtos.event.response.EventResDto;
import org.zalmoxis.evetic.mappers.EventMapper;
import org.zalmoxis.evetic.services.EventService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController
{
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<Page<EventPublishedResDto>> getAllPublishedEvents(Pageable pageable)
    {
        Page<EventPublishedResDto> events = eventService.getPublishedEvents(
                        pageable)
                .map(eventMapper::toEventPublishedResDto);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullResDto> getPublishedEventById(@PathVariable("eventId") UUID eventId)
    {
        EventFullResDto eventFullResDto = eventMapper.toEventFullResDto(
                eventService.getPublishedEventById(eventId));

        return new ResponseEntity<>(eventFullResDto, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EventPublishedResDto>> searchPublishedEvents(
            @RequestParam(required = false) String q,
            Pageable pageable)
    {
        Page<EventPublishedResDto> events = eventService.searchPublishedEvents(
                        q,
                        pageable)
                .map(eventMapper::toEventPublishedResDto);

        return new ResponseEntity<>(events, HttpStatus.OK);
    }


}
