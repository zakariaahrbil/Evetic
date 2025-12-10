package org.zalmoxis.evetic.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zalmoxis.evetic.dtos.EventCreationReqDto;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.TicketType;
import org.zalmoxis.evetic.entities.User;
import org.zalmoxis.evetic.exceptions.UserNotFoundException;
import org.zalmoxis.evetic.repositories.EventRepo;
import org.zalmoxis.evetic.repositories.UserRepo;
import org.zalmoxis.evetic.services.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl
        implements EventService
{

    private final UserRepo userRepo;
    private final EventRepo eventRepo;

    @Override
    public Event createEvent(UUID organizerId, EventCreationReqDto eventCreationReqDto)
    {
        User organizer = userRepo.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("Organizer with ID " + organizerId + " not found"));

        Event event = Event.builder()
                .name(eventCreationReqDto.getName())
                .location(eventCreationReqDto.getLocation())
                .startTime(eventCreationReqDto.getStartTime())
                .endTime(eventCreationReqDto.getEndTime())
                .startSalesAt(eventCreationReqDto.getStartSalesAt())
                .endSalesAt(eventCreationReqDto.getEndSalesAt())
                .description(eventCreationReqDto.getDescription())
                .status(eventCreationReqDto.getStatus())
                .organizer(organizer)
                .build();

        List<TicketType> ticketTypes = new ArrayList<>();
        ticketTypes = eventCreationReqDto.getTicketTypes().stream()
                .map(ticketType ->
                        TicketType.builder()
                                .name(ticketType.getName())
                                .price(ticketType.getPrice())
                                .description(ticketType.getDescription())
                                .totalAvailable(ticketType.getTotalAvailable())
                                .event(event)
                                .build()
                ).toList();

        event.setTicketTypes(ticketTypes);

        return eventRepo.save(event);
    }
}
