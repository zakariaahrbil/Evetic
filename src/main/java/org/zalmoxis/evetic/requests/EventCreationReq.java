package org.zalmoxis.evetic.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zalmoxis.evetic.entities.EventStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreationReq
{
    private String name;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime startSalesAt;
    private LocalDateTime endSalesAt;
    private EventStatusEnum status;
    // Using TicketTypeCreationReq to encapsulate ticket type data to be created along with the event
    private List<TicketTypeCreationReq> ticketTypes;

}
