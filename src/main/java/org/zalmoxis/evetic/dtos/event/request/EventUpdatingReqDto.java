package org.zalmoxis.evetic.dtos.event.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.zalmoxis.evetic.dtos.tickettype.request.TicketTypeUpdatingReqDto;
import org.zalmoxis.evetic.entities.EventStatusEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdatingReqDto
{
    private UUID id;
    @NotBlank(message = "Setting the name is mandatory")
    private String name;
    @NotBlank(message = "Setting the location is mandatory")
    private String location;
    @Size(max = 2500, message = "The description must not exceed 2500 characters")
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime startSalesAt;
    private LocalDateTime endSalesAt;
    @NotNull(message = "Setting the status is mandatory")
    private EventStatusEnum status;
    @NotEmpty(message = "At least one ticket type must be provided")
    @Valid
    private List<TicketTypeUpdatingReqDto> ticketTypes;
}
