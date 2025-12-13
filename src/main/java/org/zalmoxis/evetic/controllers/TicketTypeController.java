package org.zalmoxis.evetic.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zalmoxis.evetic.services.TicketTypeService;
import org.zalmoxis.evetic.utils.UUIDFromAuthentication;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events/{eventId}/ticket-types")
@RequiredArgsConstructor
public class TicketTypeController
{
    private final TicketTypeService ticketTypeService;
    private final UUIDFromAuthentication uuidFromAuthentication;

    @PostMapping("/{ticketTypeId}/purchase")
    public ResponseEntity<Void> purchaseTicket(@PathVariable("eventId") UUID eventId,
            @PathVariable("ticketTypeId") UUID ticketTypeId, Authentication authentication)
    {
        UUID userId = uuidFromAuthentication.getUUIDFromAuthentication(authentication);
        ticketTypeService.purchaseTicket(eventId, ticketTypeId, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
