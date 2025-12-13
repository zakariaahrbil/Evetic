package org.zalmoxis.evetic.dtos.ticket.response;

import org.zalmoxis.evetic.entities.TicketStatusEnum;
import org.zalmoxis.evetic.entities.TicketType;
import org.zalmoxis.evetic.entities.User;

import java.util.UUID;

public class TicketResDto
{
    private UUID id;
    private TicketStatusEnum status;
    private User owner;
    private TicketType ticketType;


}
