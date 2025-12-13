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
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.entities.TicketStatusEnum;
import org.zalmoxis.evetic.exceptions.TicketNotFoundException;
import org.zalmoxis.evetic.repositories.TicketRepo;
import org.zalmoxis.evetic.services.implementations.TicketServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private TicketRepo ticketRepo;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private UUID ownerId;
    private UUID ticketId;
    private Ticket ticket;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        ticketId = UUID.randomUUID();
        ticket = Ticket.builder()
                .id(ticketId)
                .status(TicketStatusEnum.PURCHASED)
                .build();
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void listAllTicketsForUser_ShouldReturnPageOfTickets() {
        Page<Ticket> expectedPage = new PageImpl<>(List.of(ticket));
        when(ticketRepo.findAllByOwnerId(ownerId, pageable)).thenReturn(expectedPage);

        Page<Ticket> result = ticketService.listAllTicketsForUser(ownerId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(ticketRepo, times(1)).findAllByOwnerId(ownerId, pageable);
    }

    @Test
    void listAllTicketsForUser_ShouldReturnEmptyPage_WhenNoTickets() {
        Page<Ticket> emptyPage = new PageImpl<>(List.of());
        when(ticketRepo.findAllByOwnerId(ownerId, pageable)).thenReturn(emptyPage);

        Page<Ticket> result = ticketService.listAllTicketsForUser(ownerId, pageable);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(ticketRepo, times(1)).findAllByOwnerId(ownerId, pageable);
    }

    @Test
    void getTicketForOwner_ShouldReturnTicket_WhenExists() {
        when(ticketRepo.findByIdAndOwnerId(ticketId, ownerId)).thenReturn(Optional.of(ticket));

        Ticket result = ticketService.getTicketForOwner(ticketId, ownerId);

        assertNotNull(result);
        assertEquals(ticketId, result.getId());
        verify(ticketRepo, times(1)).findByIdAndOwnerId(ticketId, ownerId);
    }

    @Test
    void getTicketForOwner_ShouldThrowException_WhenNotFound() {
        when(ticketRepo.findByIdAndOwnerId(ticketId, ownerId)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketForOwner(ticketId, ownerId));
        verify(ticketRepo, times(1)).findByIdAndOwnerId(ticketId, ownerId);
    }
}

