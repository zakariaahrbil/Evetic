package org.zalmoxis.evetic.dtos.ticket.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zalmoxis.evetic.entities.EventStatusEnum;
import org.zalmoxis.evetic.entities.TicketStatusEnum;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailsResDto
{
    private UUID id;
    private TicketStatusEnum status;
    private String ticketTypeName;
    private String ticketTypeDescription;
    private Double ticketTypePrice;
    private String eventName;
    private UUID eventId;
    private String eventLocation;
    private LocalDateTime createdAt;
    private LocalDateTime eventStartTime;
    private LocalDateTime eventEndTime;
    private EventStatusEnum eventStatus;
}
