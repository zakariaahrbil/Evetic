package org.zalmoxis.evetic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.zalmoxis.evetic.dtos.event.response.EventCreationResDto;
import org.zalmoxis.evetic.dtos.event.response.EventDetailsResDto;
import org.zalmoxis.evetic.dtos.event.response.EventFullResDto;
import org.zalmoxis.evetic.dtos.event.response.EventPublishedResDto;
import org.zalmoxis.evetic.dtos.event.response.EventResDto;
import org.zalmoxis.evetic.dtos.ticketType.response.TicketTypeDetailsResDto;
import org.zalmoxis.evetic.dtos.ticketType.response.TicketTypeFullResDto;
import org.zalmoxis.evetic.dtos.ticketType.response.TicketTypeResDto;
import org.zalmoxis.evetic.dtos.ticketType.response.TicketTypeCreationResDto;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.TicketType;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper
{

    EventCreationResDto toDto(Event event);

    TicketTypeCreationResDto toDto(TicketType ticketType);

    TicketTypeResDto toTicketTypeResDto(TicketType ticketType);

    EventResDto toEventResDto(Event event);

    TicketTypeDetailsResDto toTicketTypeDetailsResDto(TicketType ticketType);

    EventDetailsResDto toEventDetailsResDto(Event event);

    EventFullResDto toEventFullResDto(Event event);

    TicketTypeFullResDto toTicketTypeFullResDto(TicketType ticketType);

    EventPublishedResDto toEventPublishedResDto(Event event);
}
