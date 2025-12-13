package org.zalmoxis.evetic.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.repositories.TicketRepo;
import org.zalmoxis.evetic.services.TicketService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl
        implements TicketService
{
    private final TicketRepo ticketRepo;

    @Override
    public Page<Ticket> listAllTicketsForUser(UUID ownerId, Pageable pageable)
    {
        return  ticketRepo.findAllByOwnerId(ownerId, pageable);
    }
}
