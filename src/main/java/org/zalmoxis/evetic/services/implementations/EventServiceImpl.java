package org.zalmoxis.evetic.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zalmoxis.evetic.dtos.event.request.EventCreationReqDto;
import org.zalmoxis.evetic.dtos.event.request.EventUpdatingReqDto;
import org.zalmoxis.evetic.dtos.ticketType.request.TicketTypeUpdatingReqDto;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.TicketType;
import org.zalmoxis.evetic.entities.User;
import org.zalmoxis.evetic.exceptions.EventNotFoundException;
import org.zalmoxis.evetic.exceptions.EventUpdatingException;
import org.zalmoxis.evetic.exceptions.TicketTypeNotFoundException;
import org.zalmoxis.evetic.exceptions.UserNotFoundException;
import org.zalmoxis.evetic.repositories.EventRepo;
import org.zalmoxis.evetic.repositories.UserRepo;
import org.zalmoxis.evetic.services.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Override
    public Page<Event> getEventsForOrganizer(UUID organizerId, Pageable pageable)
    {
        return eventRepo.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Page<Event> getAllEvents(Pageable pageable)
    {
        return  eventRepo.findAll(pageable);
    }

    @Override
    public Event getEventByIdAndOrganizer(UUID eventId, UUID organizerId)
    {
        return eventRepo.findByIdAndOrganizerId(eventId, organizerId)
                .orElseThrow(() -> new EventNotFoundException("Event with ID " + eventId + " not found"));
    }

    @Override
    @Transactional
    public Event updateEventForOrganizer(UUID organizerId, EventUpdatingReqDto event, UUID eventId)
    {
        if (!event.getId().equals(eventId)){
            throw new EventUpdatingException("Cannot update event ID");
        }


        Event existingEvent = eventRepo.findByIdAndOrganizerId(event.getId(), organizerId)
                .orElseThrow(() -> new EventNotFoundException("Event with ID " + event.getId() + " not found"));


        existingEvent.setName(event.getName());
        existingEvent.setLocation(event.getLocation());
        existingEvent.setDescription(event.getDescription());
        existingEvent.setStartTime(event.getStartTime());
        existingEvent.setEndTime(event.getEndTime());
        existingEvent.setStartSalesAt(event.getStartSalesAt());
        existingEvent.setEndSalesAt(event.getEndSalesAt());
        existingEvent.setStatus(event.getStatus());

        Set<UUID> reqTicketTypesIds = event.getTicketTypes()
                .stream()
                .map(ticketTypeUpdatingReqDto ->  ticketTypeUpdatingReqDto.getId())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        existingEvent.getTicketTypes()
                .removeIf(ticketType -> !reqTicketTypesIds.contains(ticketType.getId()));

        Map<UUID,TicketType> existingTicketTypesMap = existingEvent.getTicketTypes()
                .stream()
                .collect(Collectors.toMap(TicketType::getId,Function.identity()));

        for(TicketTypeUpdatingReqDto ticketTypeUpdatingReqDto : event.getTicketTypes()) {
            if (ticketTypeUpdatingReqDto.getId() == null) {
                TicketType newTicketType = TicketType.builder()
                        .name(ticketTypeUpdatingReqDto.getName())
                        .price(ticketTypeUpdatingReqDto.getPrice())
                        .description(ticketTypeUpdatingReqDto.getDescription())
                        .totalAvailable(ticketTypeUpdatingReqDto.getTotalAvailable())
                        .event(existingEvent)
                        .build();
                existingEvent.getTicketTypes().add(newTicketType);
            } else {
                TicketType existingTicketType = existingTicketTypesMap.get(ticketTypeUpdatingReqDto.getId());
                if (existingTicketType != null) {
                    existingTicketType.setName(ticketTypeUpdatingReqDto.getName());
                    existingTicketType.setPrice(ticketTypeUpdatingReqDto.getPrice());
                    existingTicketType.setDescription(ticketTypeUpdatingReqDto.getDescription());
                    existingTicketType.setTotalAvailable(ticketTypeUpdatingReqDto.getTotalAvailable());
                } else {
                    throw new TicketTypeNotFoundException("Ticket Type with ID " + ticketTypeUpdatingReqDto.getId() + " not found");
                }
            }
        }

        return eventRepo.save(existingEvent);
    }
}
    