package org.zalmoxis.evetic.dtos.event.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPublishedResDto
{
    private UUID id;
    private String name;
    private String location;
    private String description;

}
