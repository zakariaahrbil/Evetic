package org.zalmoxis.evetic.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeCreationReq
{
    private String name;
    private String description;
    private Double price;
    private Integer totalAvailable;


}
