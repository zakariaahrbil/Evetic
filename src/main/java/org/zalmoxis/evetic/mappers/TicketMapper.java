package org.zalmoxis.evetic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.zalmoxis.evetic.dtos.ticket.response.TicketDetailsResDto;
import org.zalmoxis.evetic.dtos.ticket.response.TicketResDto;
import org.zalmoxis.evetic.entities.Ticket;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper
{
    @Mapping(target = "ticketTypeName" , source = "ticket.ticketType.name")
    @Mapping(target = "ticketTypeDescription" , source = "ticket.ticketType.description")
    @Mapping(target = "ticketTypePrice" , source = "ticket.ticketType.price")
    @Mapping(target = "eventName" , source = "ticket.ticketType.event.name")
    @Mapping(target = "eventId" , source = "ticket.ticketType.event.id")
    TicketResDto toTicketResDto(Ticket ticket);


    @Mapping(target = "ticketTypeName", source = "ticket.ticketType.name")
    @Mapping(target = "ticketTypeDescription", source = "ticket.ticketType.description")
    @Mapping(target = "ticketTypePrice", source = "ticket.ticketType.price")
    @Mapping(target = "eventName", source = "ticket.ticketType.event.name")
    @Mapping(target = "eventId", source = "ticket.ticketType.event.id")
    @Mapping(target = "eventLocation", source = "ticket.ticketType.event.location")
    @Mapping(target = "eventStartTime", source = "ticket.ticketType.event.startTime")
    @Mapping(target = "eventEndTime", source = "ticket.ticketType.event.endTime")
    @Mapping(target = "eventStatus", source = "ticket.ticketType.event.status")
    TicketDetailsResDto toTicketDetailsResDto(Ticket ticket);

}
