package org.zalmoxis.evetic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zalmoxis.evetic.entities.Event;

import java.util.UUID;

@Repository
public interface EventRepo
        extends JpaRepository<Event, UUID>
{
}
