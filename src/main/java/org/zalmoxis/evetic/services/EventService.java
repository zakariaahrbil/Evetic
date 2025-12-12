package org.zalmoxis.evetic.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zalmoxis.evetic.dtos.event.request.EventCreationReqDto;
import org.zalmoxis.evetic.dtos.event.request.EventUpdatingReqDto;
import org.zalmoxis.evetic.entities.Event;

import java.util.UUID;

public interface EventService
{
    Event createEvent(UUID organizerId, EventCreationReqDto eventCreationReqDto);

    Page<Event> getEventsForOrganizer(UUID organizerId, Pageable pageable);
    Event getEventByIdAndOrganizer(UUID eventId, UUID organizerId);
    Event updateEventForOrganizer(UUID organizerId, EventUpdatingReqDto event, UUID eventId);
    void deleteEventForOrganizer(UUID organizerId, UUID eventId);
    Page<Event> getPublishedEvents(Pageable pageable);
    Event getPublishedEventById(UUID eventId);
    Page<Event> searchPublishedEvents(String query, Pageable pageable);
}
