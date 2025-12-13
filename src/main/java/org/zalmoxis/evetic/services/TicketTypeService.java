package org.zalmoxis.evetic.services;

import org.zalmoxis.evetic.entities.Ticket;

import java.util.UUID;

public interface TicketTypeService
{
    Ticket purchaseTicket(UUID eventId, UUID ticketTypeId, UUID userId);
}
