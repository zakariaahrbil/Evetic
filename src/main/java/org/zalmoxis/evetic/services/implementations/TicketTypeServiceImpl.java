package org.zalmoxis.evetic.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zalmoxis.evetic.entities.EventStatusEnum;
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.entities.TicketStatusEnum;
import org.zalmoxis.evetic.entities.TicketType;
import org.zalmoxis.evetic.entities.User;
import org.zalmoxis.evetic.exceptions.TicketTypeNotFoundException;
import org.zalmoxis.evetic.exceptions.TicketsSoldOutException;
import org.zalmoxis.evetic.exceptions.UserNotAuthorized;
import org.zalmoxis.evetic.exceptions.UserNotFoundException;
import org.zalmoxis.evetic.repositories.EventRepo;
import org.zalmoxis.evetic.repositories.TicketRepo;
import org.zalmoxis.evetic.repositories.TicketTypeRepo;
import org.zalmoxis.evetic.repositories.UserRepo;
import org.zalmoxis.evetic.services.QrCodeService;
import org.zalmoxis.evetic.services.TicketTypeService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl
        implements TicketTypeService
{
    private final UserRepo userRepo;
    private final TicketTypeRepo ticketTypeRepo;
    private  final TicketRepo ticketRepo;
    private final QrCodeService qrCodeService;
    private final EventRepo eventRepo;

    @Override
    @Transactional
    public Ticket purchaseTicket(UUID eventId, UUID ticketTypeId, UUID userId)
    {
        User purchaser = userRepo.findById(userId)
                .orElseThrow(() ->  new UserNotFoundException("User with ID " + userId + " not found"));

        if(!eventRepo.existsByIdAndStatus(eventId, EventStatusEnum.PUBLISHED)){
            throw new UserNotAuthorized("Unauthorized to purchase tickets for this event");
        }

        TicketType ticketType = ticketTypeRepo.findWithLockById(ticketTypeId)
                .orElseThrow(() -> new TicketTypeNotFoundException("Ticket type with ID " + ticketTypeId + " not found"));

        int purchasedTicketsCount = ticketRepo.countByTicketTypeId(ticketTypeId);
        int availableTickets = ticketType.getTotalAvailable() - purchasedTicketsCount;

        if(availableTickets <= 0){
            throw new TicketsSoldOutException("No tickets available for ticket type ID " + ticketTypeId);
        }

        Ticket newTicket = Ticket.builder()
                .ticketType(ticketType)
                .owner(purchaser)
                .status(TicketStatusEnum.PURCHASED)
                .build();

        Ticket updatedTicket = ticketRepo.save(newTicket);

        qrCodeService.generateQrCode(updatedTicket);

        return ticketRepo.save(updatedTicket);

    }
}
