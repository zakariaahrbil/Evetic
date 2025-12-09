package org.zalmoxis.evetic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.zalmoxis.evetic.dtos.EventCreationReqDto;
import org.zalmoxis.evetic.dtos.EventCreationResDto;
import org.zalmoxis.evetic.dtos.TicketTypeCreationReqDto;
import org.zalmoxis.evetic.dtos.TicketTypeCreationResDto;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.TicketType;
import org.zalmoxis.evetic.requests.EventCreationReq;
import org.zalmoxis.evetic.requests.TicketTypeCreationReq;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper
{

    TicketTypeCreationReq fromDto(TicketTypeCreationReqDto ticketTypeCreationReqDto);

    EventCreationReq fromDto(EventCreationReqDto eventCreationReqDto);

    EventCreationResDto toDto(Event event);

    TicketTypeCreationResDto toDto(TicketType ticketType);
}
