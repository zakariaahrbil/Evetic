package org.zalmoxis.evetic.dtos.ticketType.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketTypeUpdatingReqDto
{
    private UUID id;
    @NotBlank(message = "Setting the name is mandatory")
    private String name;
    @Size(max = 500, message = "The description must not exceed 500 characters")
    private String description;
    @NotNull(message = "Setting the price is mandatory")
    @PositiveOrZero(message = "The price must be positive or zero")
    private Double price;
    private Integer totalAvailable;
}
