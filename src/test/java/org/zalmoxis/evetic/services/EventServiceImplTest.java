package org.zalmoxis.evetic.services;

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
import org.zalmoxis.evetic.dtos.event.request.EventCreationReqDto;
import org.zalmoxis.evetic.dtos.event.request.EventUpdatingReqDto;
import org.zalmoxis.evetic.dtos.tickettype.request.TicketTypeCreationReqDto;
import org.zalmoxis.evetic.dtos.tickettype.request.TicketTypeUpdatingReqDto;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.EventStatusEnum;
import org.zalmoxis.evetic.entities.TicketType;
import org.zalmoxis.evetic.entities.User;
import org.zalmoxis.evetic.exceptions.EventNotFoundException;
import org.zalmoxis.evetic.exceptions.EventUpdatingException;
import org.zalmoxis.evetic.exceptions.UserNotFoundException;
import org.zalmoxis.evetic.repositories.EventRepo;
import org.zalmoxis.evetic.repositories.UserRepo;
import org.zalmoxis.evetic.services.implementations.EventServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private EventRepo eventRepo;

    @InjectMocks
    private EventServiceImpl eventService;

    private UUID organizerId;
    private UUID eventId;
    private User organizer;
    private Event event;
    private Pageable pageable;
    private EventCreationReqDto eventCreationReqDto;

    @BeforeEach
    void setUp() {
        organizerId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);

        organizer = User.builder()
                .id(organizerId)
                .username("organizer")
                .email("organizer@test.com")
                .build();

        event = Event.builder()
                .id(eventId)
                .name("Test Event")
                .location("Test Location")
                .description("Test Description")
                .status(EventStatusEnum.DRAFT)
                .organizer(organizer)
                .ticketTypes(new ArrayList<>())
                .build();

        TicketTypeCreationReqDto ticketTypeDto = new TicketTypeCreationReqDto();
        ticketTypeDto.setName("VIP");
        ticketTypeDto.setPrice(100.0);
        ticketTypeDto.setTotalAvailable(50);
        ticketTypeDto.setDescription("VIP Ticket");

        eventCreationReqDto = new EventCreationReqDto();
        eventCreationReqDto.setName("New Event");
        eventCreationReqDto.setLocation("New Location");
        eventCreationReqDto.setDescription("New Description");
        eventCreationReqDto.setStatus(EventStatusEnum.DRAFT);
        eventCreationReqDto.setTicketTypes(List.of(ticketTypeDto));
    }

    @Test
    void createEvent_ShouldCreateEvent_WhenOrganizerExists() {
        when(userRepo.findById(organizerId)).thenReturn(Optional.of(organizer));
        when(eventRepo.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event result = eventService.createEvent(organizerId, eventCreationReqDto);

        assertNotNull(result);
        assertEquals("New Event", result.getName());
        assertEquals("New Location", result.getLocation());
        verify(userRepo, times(1)).findById(organizerId);
        verify(eventRepo, times(1)).save(any(Event.class));
    }

    @Test
    void createEvent_ShouldThrowException_WhenOrganizerNotFound() {
        when(userRepo.findById(organizerId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> eventService.createEvent(organizerId, eventCreationReqDto));
        verify(eventRepo, never()).save(any(Event.class));
    }

    @Test
    void getEventsForOrganizer_ShouldReturnPageOfEvents() {
        Page<Event> expectedPage = new PageImpl<>(List.of(event));
        when(eventRepo.findByOrganizerId(organizerId, pageable)).thenReturn(expectedPage);

        Page<Event> result = eventService.getEventsForOrganizer(organizerId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(eventRepo, times(1)).findByOrganizerId(organizerId, pageable);
    }

    @Test
    void getPublishedEvents_ShouldReturnPublishedEvents() {
        event.setStatus(EventStatusEnum.PUBLISHED);
        Page<Event> expectedPage = new PageImpl<>(List.of(event));
        when(eventRepo.findByStatus(EventStatusEnum.PUBLISHED, pageable)).thenReturn(expectedPage);

        Page<Event> result = eventService.getPublishedEvents(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(eventRepo, times(1)).findByStatus(EventStatusEnum.PUBLISHED, pageable);
    }

    @Test
    void getPublishedEventById_ShouldReturnEvent_WhenExists() {
        event.setStatus(EventStatusEnum.PUBLISHED);
        when(eventRepo.findByIdAndStatus(eventId, EventStatusEnum.PUBLISHED)).thenReturn(Optional.of(event));

        Event result = eventService.getPublishedEventById(eventId);

        assertNotNull(result);
        assertEquals(eventId, result.getId());
        verify(eventRepo, times(1)).findByIdAndStatus(eventId, EventStatusEnum.PUBLISHED);
    }

    @Test
    void getPublishedEventById_ShouldThrowException_WhenNotFound() {
        when(eventRepo.findByIdAndStatus(eventId, EventStatusEnum.PUBLISHED)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.getPublishedEventById(eventId));
    }

    @Test
    void searchPublishedEvents_ShouldReturnResults_WhenQueryProvided() {
        Page<Event> expectedPage = new PageImpl<>(List.of(event));
        when(eventRepo.searchPublishedEvents("Test", pageable)).thenReturn(expectedPage);

        Page<Event> result = eventService.searchPublishedEvents("Test", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(eventRepo, times(1)).searchPublishedEvents("Test", pageable);
    }

    @Test
    void searchPublishedEvents_ShouldReturnAllPublished_WhenQueryIsEmpty() {
        Page<Event> expectedPage = new PageImpl<>(List.of(event));
        when(eventRepo.findByStatus(EventStatusEnum.PUBLISHED, pageable)).thenReturn(expectedPage);

        Page<Event> result = eventService.searchPublishedEvents("", pageable);

        assertNotNull(result);
        verify(eventRepo, times(1)).findByStatus(EventStatusEnum.PUBLISHED, pageable);
    }

    @Test
    void searchPublishedEvents_ShouldReturnAllPublished_WhenQueryIsNull() {
        Page<Event> expectedPage = new PageImpl<>(List.of(event));
        when(eventRepo.findByStatus(EventStatusEnum.PUBLISHED, pageable)).thenReturn(expectedPage);

        Page<Event> result = eventService.searchPublishedEvents(null, pageable);

        assertNotNull(result);
        verify(eventRepo, times(1)).findByStatus(EventStatusEnum.PUBLISHED, pageable);
    }

    @Test
    void getEventByIdAndOrganizer_ShouldReturnEvent_WhenExists() {
        when(eventRepo.findByIdAndOrganizerId(eventId, organizerId)).thenReturn(Optional.of(event));

        Event result = eventService.getEventByIdAndOrganizer(eventId, organizerId);

        assertNotNull(result);
        assertEquals(eventId, result.getId());
        verify(eventRepo, times(1)).findByIdAndOrganizerId(eventId, organizerId);
    }

    @Test
    void getEventByIdAndOrganizer_ShouldThrowException_WhenNotFound() {
        when(eventRepo.findByIdAndOrganizerId(eventId, organizerId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.getEventByIdAndOrganizer(eventId, organizerId));
    }

    @Test
    void deleteEventForOrganizer_ShouldDeleteEvent_WhenExists() {
        when(eventRepo.findByIdAndOrganizerId(eventId, organizerId)).thenReturn(Optional.of(event));
        doNothing().when(eventRepo).delete(event);

        eventService.deleteEventForOrganizer(organizerId, eventId);

        verify(eventRepo, times(1)).findByIdAndOrganizerId(eventId, organizerId);
        verify(eventRepo, times(1)).delete(event);
    }

    @Test
    void deleteEventForOrganizer_ShouldThrowException_WhenNotFound() {
        when(eventRepo.findByIdAndOrganizerId(eventId, organizerId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.deleteEventForOrganizer(organizerId, eventId));
        verify(eventRepo, never()).delete(any(Event.class));
    }

    @Test
    void updateEventForOrganizer_ShouldThrowException_WhenEventIdMismatch() {
        UUID differentEventId = UUID.randomUUID();
        EventUpdatingReqDto updateDto = new EventUpdatingReqDto();
        updateDto.setId(eventId);

        assertThrows(EventUpdatingException.class,
            () -> eventService.updateEventForOrganizer(organizerId, updateDto, differentEventId));
    }

    @Test
    void updateEventForOrganizer_ShouldThrowException_WhenEventNotFound() {
        EventUpdatingReqDto updateDto = new EventUpdatingReqDto();
        updateDto.setId(eventId);
        updateDto.setName("Updated Name");
        updateDto.setTicketTypes(new ArrayList<>());

        when(eventRepo.findByIdAndOrganizerId(eventId, organizerId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
            () -> eventService.updateEventForOrganizer(organizerId, updateDto, eventId));
    }

    @Test
    void updateEventForOrganizer_ShouldUpdateEvent_WhenValid() {
        UUID ticketTypeId = UUID.randomUUID();
        TicketType existingTicketType = TicketType.builder()
                .id(ticketTypeId)
                .name("Old VIP")
                .price(100.0)
                .build();
        event.setTicketTypes(new ArrayList<>(List.of(existingTicketType)));

        TicketTypeUpdatingReqDto ticketTypeDto = new TicketTypeUpdatingReqDto();
        ticketTypeDto.setId(ticketTypeId);
        ticketTypeDto.setName("Updated VIP");
        ticketTypeDto.setPrice(150.0);
        ticketTypeDto.setTotalAvailable(100);

        EventUpdatingReqDto updateDto = new EventUpdatingReqDto();
        updateDto.setId(eventId);
        updateDto.setName("Updated Event");
        updateDto.setLocation("Updated Location");
        updateDto.setDescription("Updated Description");
        updateDto.setStatus(EventStatusEnum.PUBLISHED);
        updateDto.setTicketTypes(List.of(ticketTypeDto));

        when(eventRepo.findByIdAndOrganizerId(eventId, organizerId)).thenReturn(Optional.of(event));
        when(eventRepo.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Event result = eventService.updateEventForOrganizer(organizerId, updateDto, eventId);

        assertNotNull(result);
        assertEquals("Updated Event", result.getName());
        assertEquals("Updated Location", result.getLocation());
        verify(eventRepo, times(1)).save(any(Event.class));
    }
}

