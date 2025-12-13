package org.zalmoxis.evetic.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zalmoxis.evetic.entities.Ticket;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepo
        extends JpaRepository<Ticket, UUID>
{
    int countByTicketTypeId(UUID ticketTypeId);
    Page<Ticket> findAllByOwnerId(UUID ownerId, Pageable pageable);
    Optional<Ticket> findByIdAndOwnerId(UUID ticketId, UUID ownerId);
}
