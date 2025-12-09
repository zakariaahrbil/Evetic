package org.zalmoxis.evetic.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.TicketType;
import org.zalmoxis.evetic.entities.User;
import org.zalmoxis.evetic.exceptions.UserNotFoundException;
import org.zalmoxis.evetic.repositories.EventRepo;
import org.zalmoxis.evetic.repositories.UserRepo;
import org.zalmoxis.evetic.requests.EventCreationReq;
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
    public Event createEvent(UUID organizerId, EventCreationReq eventCreationReq)
    {
        User organizer = userRepo.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("Organizer with ID " + organizerId + " not found"));

        Event event = Event.builder()
                .name(eventCreationReq.getName())
                .location(eventCreationReq.getLocation())
                .startTime(eventCreationReq.getStartTime())
                .endTime(eventCreationReq.getEndTime())
                .startSalesAt(eventCreationReq.getStartSalesAt())
                .endSalesAt(eventCreationReq.getEndSalesAt())
                .status(eventCreationReq.getStatus())
                .organizer(organizer)
                .build();

        List<TicketType> ticketTypes = new ArrayList<>();
        ticketTypes = eventCreationReq.getTicketTypes().stream()
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
