package org.zalmoxis.evetic.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zalmoxis.evetic.entities.Event;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepo
        extends JpaRepository<Event, UUID>
{
    Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);
    Optional<Event> findByIdAndOrganizerId(UUID eventId, UUID organizerId);
}
