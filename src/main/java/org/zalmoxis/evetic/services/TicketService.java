package org.zalmoxis.evetic.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zalmoxis.evetic.entities.Ticket;

import java.util.UUID;

public interface TicketService
{
    Page<Ticket> listAllTicketsForUser(UUID ownerId, Pageable pageable);
    Ticket getTicketForOwner(UUID ticketId, UUID ownerId);
}
