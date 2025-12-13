package org.zalmoxis.evetic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalmoxis.evetic.dtos.ticket.response.TicketResDto;
import org.zalmoxis.evetic.mappers.TicketMapper;
import org.zalmoxis.evetic.services.TicketService;
import org.zalmoxis.evetic.utils.UUIDFromAuthentication;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController
{
    private final UUIDFromAuthentication uuidFromAuthentication;
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    @GetMapping
    public ResponseEntity<Page<TicketResDto>> listAllTicketsForUser(Pageable pageable,
            Authentication authentication)
    {
        Page<TicketResDto> tickets = ticketService.listAllTicketsForUser(uuidFromAuthentication.getUUIDFromAuthentication(authentication),
                pageable).map(ticketMapper::toTicketResDto);

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}
