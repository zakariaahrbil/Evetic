package org.zalmoxis.evetic.dtos.ticketvalidation.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zalmoxis.evetic.entities.TicketValidationMethodEnum;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketValidationReqDto
{
    @NotNull(message = "ID cannot be null")
    private UUID id;
    @NotNull(message = "Ticket validation method cannot be null")
    private TicketValidationMethodEnum ticketValidationMethod;
}
