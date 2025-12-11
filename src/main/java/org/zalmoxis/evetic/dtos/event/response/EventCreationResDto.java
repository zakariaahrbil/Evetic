package org.zalmoxis.evetic.dtos.event.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zalmoxis.evetic.dtos.ticketType.response.TicketTypeCreationResDto;
import org.zalmoxis.evetic.entities.EventStatusEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreationResDto
{
    private UUID id;
    private String name;
    private String location;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime startSalesAt;
    private LocalDateTime endSalesAt;
    private EventStatusEnum status;
    private List<TicketTypeCreationResDto> ticketTypes = new ArrayList<>();

}
