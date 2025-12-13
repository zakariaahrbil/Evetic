package org.zalmoxis.evetic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.zalmoxis.evetic.dtos.event.response.EventTicketResDto;
import org.zalmoxis.evetic.dtos.ticket.response.TicketResDto;
import org.zalmoxis.evetic.dtos.ticketType.response.TicketTypeTicketResDto;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.entities.TicketType;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper
{
    TicketResDto toTicketResDto(Ticket ticket);
    TicketTypeTicketResDto toTicketTypeTicketResDto(TicketType ticketType);

    EventTicketResDto toEventTicketResDto(Event event);
}
