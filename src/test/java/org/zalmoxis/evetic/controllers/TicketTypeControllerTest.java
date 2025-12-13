package org.zalmoxis.evetic.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.services.TicketTypeService;
import org.zalmoxis.evetic.utils.UUIDFromAuthentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketTypeControllerTest {

    @Mock
    private TicketTypeService ticketTypeService;

    @Mock
    private UUIDFromAuthentication uuidFromAuthentication;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TicketTypeController ticketTypeController;

    private UUID eventId;
    private UUID ticketTypeId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        eventId = UUID.randomUUID();
        ticketTypeId = UUID.randomUUID();
        userId = UUID.randomUUID();
    }

    @Test
    void purchaseTicket_ShouldReturnNoContent_WhenSuccessful() {
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .build();

        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(userId);
        when(ticketTypeService.purchaseTicket(eventId, ticketTypeId, userId)).thenReturn(ticket);

        ResponseEntity<Void> response = ticketTypeController.purchaseTicket(eventId, ticketTypeId, authentication);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(uuidFromAuthentication, times(1)).getUUIDFromAuthentication(authentication);
        verify(ticketTypeService, times(1)).purchaseTicket(eventId, ticketTypeId, userId);
    }

    @Test
    void purchaseTicket_ShouldCallServiceWithCorrectParameters() {
        Ticket ticket = Ticket.builder().build();

        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(userId);
        when(ticketTypeService.purchaseTicket(eventId, ticketTypeId, userId)).thenReturn(ticket);

        ticketTypeController.purchaseTicket(eventId, ticketTypeId, authentication);

        verify(ticketTypeService).purchaseTicket(eventId, ticketTypeId, userId);
    }
}

