package org.zalmoxis.evetic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.zalmoxis.evetic.dtos.event.response.EventFullResDto;
import org.zalmoxis.evetic.dtos.event.response.EventPublishedResDto;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.EventStatusEnum;
import org.zalmoxis.evetic.mappers.EventMapper;
import org.zalmoxis.evetic.services.EventService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublishedEventControllerTest {

    @Mock
    private EventService eventService;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private PublishedEventController publishedEventController;

    private UUID eventId;
    private Event event;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);

        event = Event.builder()
                .id(eventId)
                .name("Published Event")
                .location("Event Location")
                .status(EventStatusEnum.PUBLISHED)
                .build();
    }

    @Test
    void getAllPublishedEvents_ShouldReturnPageOfEvents() {
        Page<Event> eventPage = new PageImpl<>(List.of(event));
        EventPublishedResDto publishedResDto = new EventPublishedResDto();
        publishedResDto.setId(eventId);
        publishedResDto.setName("Published Event");

        when(eventService.getPublishedEvents(pageable)).thenReturn(eventPage);
        when(eventMapper.toEventPublishedResDto(event)).thenReturn(publishedResDto);

        ResponseEntity<Page<EventPublishedResDto>> response = publishedEventController.getAllPublishedEvents(pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(eventService, times(1)).getPublishedEvents(pageable);
    }

    @Test
    void getPublishedEventById_ShouldReturnEventFullDetails() {
        EventFullResDto fullResDto = new EventFullResDto();
        fullResDto.setId(eventId);
        fullResDto.setName("Published Event");

        when(eventService.getPublishedEventById(eventId)).thenReturn(event);
        when(eventMapper.toEventFullResDto(event)).thenReturn(fullResDto);

        ResponseEntity<EventFullResDto> response = publishedEventController.getPublishedEventById(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventId, response.getBody().getId());
        verify(eventService, times(1)).getPublishedEventById(eventId);
    }

    @Test
    void searchPublishedEvents_ShouldReturnSearchResults() {
        String query = "concert";
        Page<Event> eventPage = new PageImpl<>(List.of(event));
        EventPublishedResDto publishedResDto = new EventPublishedResDto();
        publishedResDto.setId(eventId);

        when(eventService.searchPublishedEvents(query, pageable)).thenReturn(eventPage);
        when(eventMapper.toEventPublishedResDto(event)).thenReturn(publishedResDto);

        ResponseEntity<Page<EventPublishedResDto>> response = publishedEventController.searchPublishedEvents(query, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(eventService, times(1)).searchPublishedEvents(query, pageable);
    }

    @Test
    void searchPublishedEvents_ShouldHandleNullQuery() {
        Page<Event> eventPage = new PageImpl<>(List.of(event));
        EventPublishedResDto publishedResDto = new EventPublishedResDto();
        publishedResDto.setId(eventId);

        when(eventService.searchPublishedEvents(null, pageable)).thenReturn(eventPage);
        when(eventMapper.toEventPublishedResDto(event)).thenReturn(publishedResDto);

        ResponseEntity<Page<EventPublishedResDto>> response = publishedEventController.searchPublishedEvents(null, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(eventService, times(1)).searchPublishedEvents(null, pageable);
    }
}

