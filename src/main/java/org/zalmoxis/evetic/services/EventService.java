package org.zalmoxis.evetic.services;

import org.zalmoxis.evetic.dtos.EventCreationReqDto;
import org.zalmoxis.evetic.entities.Event;

import java.util.UUID;

public interface EventService
{
    Event createEvent(UUID organizerId, EventCreationReqDto eventCreationReqDto);
}
