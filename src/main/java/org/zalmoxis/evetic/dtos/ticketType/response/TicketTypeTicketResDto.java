package org.zalmoxis.evetic.dtos.ticketType.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zalmoxis.evetic.dtos.event.response.EventTicketResDto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeTicketResDto
{
    private String name;
    private String description;
    private Double price;
    private EventTicketResDto event;
}
