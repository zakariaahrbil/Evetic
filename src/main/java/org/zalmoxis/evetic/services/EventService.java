package org.zalmoxis.evetic.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zalmoxis.evetic.dtos.EventCreationReqDto;
import org.zalmoxis.evetic.entities.Event;

import java.util.List;
import java.util.UUID;

public interface EventService
{
    Event createEvent(UUID organizerId, EventCreationReqDto eventCreationReqDto);

    Page<Event> getEventsForOrganizer(UUID organizerId, Pageable pageable);
}
