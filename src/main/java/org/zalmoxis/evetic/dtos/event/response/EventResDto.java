package org.zalmoxis.evetic.dtos.event.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zalmoxis.evetic.entities.EventStatusEnum;


import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventResDto
{
    private UUID id;
    private String name;
    private String location;
    private String description;
    private EventStatusEnum status;

}
