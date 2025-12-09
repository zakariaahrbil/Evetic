package org.zalmoxis.evetic.services;

import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.requests.EventCreationReq;

import java.util.UUID;

public interface EventService
{
    Event createEvent(UUID organizerId, EventCreationReq eventCreationReq);
}
