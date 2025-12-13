package org.zalmoxis.evetic.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.EventStatusEnum;
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.entities.TicketStatusEnum;
import org.zalmoxis.evetic.entities.TicketType;
import org.zalmoxis.evetic.entities.User;
import org.zalmoxis.evetic.exceptions.TicketTypeNotFoundException;
import org.zalmoxis.evetic.exceptions.TicketsSoldOutException;
import org.zalmoxis.evetic.exceptions.UserNotAuthorized;
import org.zalmoxis.evetic.exceptions.UserNotFoundException;
import org.zalmoxis.evetic.repositories.EventRepo;
import org.zalmoxis.evetic.repositories.TicketRepo;
import org.zalmoxis.evetic.repositories.TicketTypeRepo;
import org.zalmoxis.evetic.repositories.UserRepo;
import org.zalmoxis.evetic.services.implementations.TicketTypeServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketTypeServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private TicketTypeRepo ticketTypeRepo;

    @Mock
    private TicketRepo ticketRepo;

    @Mock
    private QrCodeService qrCodeService;

    @Mock
    private EventRepo eventRepo;

    @InjectMocks
    private TicketTypeServiceImpl ticketTypeService;

    private UUID eventId;
    private UUID ticketTypeId;
    private UUID userId;
    private User user;
    private TicketType ticketType;
    private Event event;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        ticketTypeId = UUID.randomUUID();
        userId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .username("testuser")
                .email("test@test.com")
                .build();

        event = Event.builder()
                .id(eventId)
                .name("Test Event")
                .status(EventStatusEnum.PUBLISHED)
                .build();

        ticketType = TicketType.builder()
                .id(ticketTypeId)
                .name("VIP")
                .price(100.0)
                .totalAvailable(50)
                .event(event)
                .build();
    }

    @Test
    void purchaseTicket_ShouldCreateTicket_WhenAvailable() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepo.existsByIdAndStatus(eventId, EventStatusEnum.PUBLISHED)).thenReturn(true);
        when(ticketTypeRepo.findWithLockById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(ticketRepo.countByTicketTypeId(ticketTypeId)).thenReturn(0);
        when(ticketRepo.save(any(Ticket.class))).thenAnswer(invocation -> {
            Ticket t = invocation.getArgument(0);
            t.setId(UUID.randomUUID());
            return t;
        });

        Ticket result = ticketTypeService.purchaseTicket(eventId, ticketTypeId, userId);

        assertNotNull(result);
        assertEquals(TicketStatusEnum.PURCHASED, result.getStatus());
        verify(qrCodeService, times(1)).generateQrCode(any(Ticket.class));
        verify(ticketRepo, times(2)).save(any(Ticket.class));
    }

    @Test
    void purchaseTicket_ShouldThrowException_WhenUserNotFound() {
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
            () -> ticketTypeService.purchaseTicket(eventId, ticketTypeId, userId));
        verify(ticketRepo, never()).save(any(Ticket.class));
    }

    @Test
    void purchaseTicket_ShouldThrowException_WhenEventNotPublished() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepo.existsByIdAndStatus(eventId, EventStatusEnum.PUBLISHED)).thenReturn(false);

        assertThrows(UserNotAuthorized.class,
            () -> ticketTypeService.purchaseTicket(eventId, ticketTypeId, userId));
        verify(ticketRepo, never()).save(any(Ticket.class));
    }

    @Test
    void purchaseTicket_ShouldThrowException_WhenTicketTypeNotFound() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepo.existsByIdAndStatus(eventId, EventStatusEnum.PUBLISHED)).thenReturn(true);
        when(ticketTypeRepo.findWithLockById(ticketTypeId)).thenReturn(Optional.empty());

        assertThrows(TicketTypeNotFoundException.class,
            () -> ticketTypeService.purchaseTicket(eventId, ticketTypeId, userId));
        verify(ticketRepo, never()).save(any(Ticket.class));
    }

    @Test
    void purchaseTicket_ShouldThrowException_WhenTicketsSoldOut() {
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepo.existsByIdAndStatus(eventId, EventStatusEnum.PUBLISHED)).thenReturn(true);
        when(ticketTypeRepo.findWithLockById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(ticketRepo.countByTicketTypeId(ticketTypeId)).thenReturn(50); // All tickets sold

        assertThrows(TicketsSoldOutException.class,
            () -> ticketTypeService.purchaseTicket(eventId, ticketTypeId, userId));
        verify(ticketRepo, never()).save(any(Ticket.class));
    }

    @Test
    void purchaseTicket_ShouldThrowException_WhenNoTicketsRemaining() {
        ticketType.setTotalAvailable(10);
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(eventRepo.existsByIdAndStatus(eventId, EventStatusEnum.PUBLISHED)).thenReturn(true);
        when(ticketTypeRepo.findWithLockById(ticketTypeId)).thenReturn(Optional.of(ticketType));
        when(ticketRepo.countByTicketTypeId(ticketTypeId)).thenReturn(10); // All 10 tickets sold

        assertThrows(TicketsSoldOutException.class,
            () -> ticketTypeService.purchaseTicket(eventId, ticketTypeId, userId));
    }
}

