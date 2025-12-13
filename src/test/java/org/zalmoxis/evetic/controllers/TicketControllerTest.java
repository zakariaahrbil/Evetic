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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.zalmoxis.evetic.dtos.ticket.response.TicketDetailsResDto;
import org.zalmoxis.evetic.dtos.ticket.response.TicketResDto;
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.entities.TicketStatusEnum;
import org.zalmoxis.evetic.mappers.TicketMapper;
import org.zalmoxis.evetic.services.QrCodeService;
import org.zalmoxis.evetic.services.TicketService;
import org.zalmoxis.evetic.utils.UUIDFromAuthentication;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketControllerTest {

    @Mock
    private UUIDFromAuthentication uuidFromAuthentication;

    @Mock
    private TicketService ticketService;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private QrCodeService qrCodeService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private TicketController ticketController;

    private UUID userId;
    private UUID ticketId;
    private Ticket ticket;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        ticketId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);

        ticket = Ticket.builder()
                .id(ticketId)
                .status(TicketStatusEnum.PURCHASED)
                .build();
    }

    @Test
    void listAllTicketsForUser_ShouldReturnPageOfTickets() {
        Page<Ticket> ticketPage = new PageImpl<>(List.of(ticket));
        TicketResDto ticketResDto = new TicketResDto();
        ticketResDto.setId(ticketId);

        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(userId);
        when(ticketService.listAllTicketsForUser(userId, pageable)).thenReturn(ticketPage);
        when(ticketMapper.toTicketResDto(ticket)).thenReturn(ticketResDto);

        ResponseEntity<Page<TicketResDto>> response = ticketController.listAllTicketsForUser(pageable, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(ticketService, times(1)).listAllTicketsForUser(userId, pageable);
    }

    @Test
    void listAllTicketsForUser_ShouldReturnEmptyPage_WhenNoTickets() {
        Page<Ticket> emptyPage = new PageImpl<>(List.of());

        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(userId);
        when(ticketService.listAllTicketsForUser(userId, pageable)).thenReturn(emptyPage);

        ResponseEntity<Page<TicketResDto>> response = ticketController.listAllTicketsForUser(pageable, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getTotalElements());
    }

    @Test
    void getTicketForOwner_ShouldReturnTicketDetails() {
        TicketDetailsResDto detailsResDto = new TicketDetailsResDto();
        detailsResDto.setId(ticketId);
        detailsResDto.setStatus(TicketStatusEnum.PURCHASED);

        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(userId);
        when(ticketService.getTicketForOwner(ticketId, userId)).thenReturn(ticket);
        when(ticketMapper.toTicketDetailsResDto(ticket)).thenReturn(detailsResDto);

        ResponseEntity<TicketDetailsResDto> response = ticketController.getTicketForOwner(ticketId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ticketId, response.getBody().getId());
        assertEquals(TicketStatusEnum.PURCHASED, response.getBody().getStatus());
        verify(ticketService, times(1)).getTicketForOwner(ticketId, userId);
    }

    @Test
    void getTicketQrCode_ShouldReturnQrCodeImage() {
        byte[] qrCodeImage = new byte[]{1, 2, 3, 4, 5};

        when(uuidFromAuthentication.getUUIDFromAuthentication(authentication)).thenReturn(userId);
        when(qrCodeService.getQrCodeImageForOwner(ticketId, userId)).thenReturn(qrCodeImage);

        ResponseEntity<byte[]> response = ticketController.getTicketQrCode(ticketId, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().length);
        assertEquals(MediaType.IMAGE_PNG, response.getHeaders().getContentType());
        assertEquals(5, response.getHeaders().getContentLength());
        verify(qrCodeService, times(1)).getQrCodeImageForOwner(ticketId, userId);
    }
}

