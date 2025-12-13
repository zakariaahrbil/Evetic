package org.zalmoxis.evetic.dtos.ticketvalidation.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zalmoxis.evetic.entities.TicketValidationStatusEnum;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketValidationResDto
{
    private UUID ticketId;
    private TicketValidationStatusEnum validationStatus;
}
