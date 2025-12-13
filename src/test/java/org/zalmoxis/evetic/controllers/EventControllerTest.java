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
import org.springframework.security.core.Authentication;
import org.zalmoxis.evetic.dtos.event.request.EventCreationReqDto;
import org.zalmoxis.evetic.dtos.event.request.EventUpdatingReqDto;
import org.zalmoxis.evetic.dtos.event.response.EventCreationResDto;
import org.zalmoxis.evetic.dtos.event.response.EventDetailsResDto;
import org.zalmoxis.evetic.dtos.event.response.EventResDto;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.EventStatusEnum;
import org.zalmoxis.evetic.mappers.EventMapper;
import org.zalmoxis.evetic.services.EventService;
import org.zalmoxis.evetic.utils.UUIDFromAuthentication;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private UUIDFromAuthentication uuidFromAuthentication;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private EventController eventController;

    private UUID organizerId;
    private UUID eventId;
    private Event event;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        organizerId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);

        event = Event.builder()
                .id(eventId)
                .name("Test Event")
                .location("Test Location")
                .status(EventStatusEnum.DRAFT)
                .build();
    }

    @Test
    void createEvent_ShouldReturnCreatedEvent() {
        EventCreationReqDto reqDto = new EventCreationReqDto();
        EventCreationResDto resDto = new EventCreationResDto();
        resDto.setId(eventId);

        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(organizerId);
        when(eventService.createEvent(eq(organizerId), any(EventCreationReqDto.class))).thenReturn(event);
        when(eventMapper.toDto(event)).thenReturn(resDto);

        ResponseEntity<EventCreationResDto> response = eventController.createEvent(reqDto, authentication);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventId, response.getBody().getId());
        verify(eventService, times(1)).createEvent(eq(organizerId), any(EventCreationReqDto.class));
    }

    @Test
    void getAllEventsForOrganizer_ShouldReturnPageOfEvents() {
        Page<Event> eventPage = new PageImpl<>(List.of(event));
        EventResDto eventResDto = new EventResDto();
        eventResDto.setId(eventId);

        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(organizerId);
        when(eventService.getEventsForOrganizer(organizerId, pageable)).thenReturn(eventPage);
        when(eventMapper.toEventResDto(event)).thenReturn(eventResDto);

        ResponseEntity<Page<EventResDto>> response = eventController.getAllEventsForOrganizer(authentication, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(eventService, times(1)).getEventsForOrganizer(organizerId, pageable);
    }

    @Test
    void getEventDetailsByIdAndOrganizer_ShouldReturnEventDetails() {
        EventDetailsResDto detailsResDto = new EventDetailsResDto();
        detailsResDto.setId(eventId);

        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(organizerId);
        when(eventService.getEventByIdAndOrganizer(eventId, organizerId)).thenReturn(event);
        when(eventMapper.toEventDetailsResDto(event)).thenReturn(detailsResDto);

        ResponseEntity<EventDetailsResDto> response = eventController.getEventDetailsByIdAndOrganizer(eventId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventId, response.getBody().getId());
        verify(eventService, times(1)).getEventByIdAndOrganizer(eventId, organizerId);
    }

    @Test
    void updateEvent_ShouldReturnUpdatedEvent() {
        EventUpdatingReqDto updateReqDto = new EventUpdatingReqDto();
        updateReqDto.setId(eventId);
        EventDetailsResDto detailsResDto = new EventDetailsResDto();
        detailsResDto.setId(eventId);

        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(organizerId);
        when(eventService.updateEventForOrganizer(eq(organizerId), any(EventUpdatingReqDto.class), eq(eventId))).thenReturn(event);
        when(eventMapper.toEventDetailsResDto(event)).thenReturn(detailsResDto);

        ResponseEntity<EventDetailsResDto> response = eventController.updateEvent(eventId, updateReqDto, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(eventId, response.getBody().getId());
        verify(eventService, times(1)).updateEventForOrganizer(eq(organizerId), any(EventUpdatingReqDto.class), eq(eventId));
    }

    @Test
    void deleteEvent_ShouldReturnNoContent() {
        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(organizerId);
        doNothing().when(eventService).deleteEventForOrganizer(organizerId, eventId);

        ResponseEntity<Void> response = eventController.deleteEvent(eventId, authentication);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eventService, times(1)).deleteEventForOrganizer(organizerId, eventId);
    }
}

