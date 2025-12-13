package org.zalmoxis.evetic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.zalmoxis.evetic.dtos.ticketvalidation.response.TicketValidationResDto;
import org.zalmoxis.evetic.entities.TicketValidation;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketValidationMapper
{
    @Mapping(target = "ticketId", source = "ticketValidation.ticket.id")
    @Mapping(target = "validationStatus", source = "ticketValidation.status")
    TicketValidationResDto toTicketValidationResDto(TicketValidation ticketValidation);
}
