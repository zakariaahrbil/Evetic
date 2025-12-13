package org.zalmoxis.evetic.dtos.ticket.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zalmoxis.evetic.dtos.ticketType.response.TicketTypeTicketResDto;
import org.zalmoxis.evetic.entities.TicketStatusEnum;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResDto
{
    private UUID id;
    private TicketStatusEnum status;
    private TicketTypeTicketResDto ticketType;
}
