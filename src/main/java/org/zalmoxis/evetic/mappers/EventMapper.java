package org.zalmoxis.evetic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.zalmoxis.evetic.dtos.EventCreationResDto;
import org.zalmoxis.evetic.dtos.EventResDto;
import org.zalmoxis.evetic.dtos.TicketTypeResDto;
import org.zalmoxis.evetic.dtos.TicketTypeCreationResDto;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.TicketType;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper
{

    EventCreationResDto toDto(Event event);

    TicketTypeCreationResDto toDto(TicketType ticketType);

    TicketTypeResDto toTicketTypeResDto(TicketType ticketType);

    EventResDto toEventResDto(Event event);
}
