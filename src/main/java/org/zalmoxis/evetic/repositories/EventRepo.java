package org.zalmoxis.evetic.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zalmoxis.evetic.entities.Event;
import org.zalmoxis.evetic.entities.EventStatusEnum;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepo
        extends JpaRepository<Event, UUID>
{
    Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);
    Optional<Event> findByIdAndOrganizerId(UUID eventId, UUID organizerId);
    Page<Event> findByStatus(EventStatusEnum eventStatus, Pageable pageable);
    Optional<Event> findByIdAndStatus(UUID eventId, EventStatusEnum eventStatus);

    @Query( value = "SELECT * FROM events WHERE "+
            "status = 'PUBLISHED' AND " +
            "to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(location, '') || ' ' || COALESCE(description, '')) @@ plainto_tsquery('english', :element)",
            countQuery = "SELECT COUNT(*) FROM events WHERE"+
                    "status = 'PUBLISHED' AND " +
                    "to_tsvector('english', COALESCE(name, '') || ' ' || COALESCE(location, '') || ' ' || COALESCE(description, '')) @@ plainto_tsquery('english', :element)",
            nativeQuery = true
    )
    Page<Event> searchPublishedEvents(@Param("element") String query, Pageable pageable);
    boolean existsByIdAndStatus(UUID eventId, EventStatusEnum status);
}
