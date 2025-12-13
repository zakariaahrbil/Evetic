package org.zalmoxis.evetic.services.implementations;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zalmoxis.evetic.entities.QrCode;
import org.zalmoxis.evetic.entities.QrCodeStatusEnum;
import org.zalmoxis.evetic.entities.Ticket;
import org.zalmoxis.evetic.entities.TicketValidation;
import org.zalmoxis.evetic.entities.TicketValidationMethodEnum;
import org.zalmoxis.evetic.entities.TicketValidationStatusEnum;
import org.zalmoxis.evetic.exceptions.QrCodeInvalidException;
import org.zalmoxis.evetic.exceptions.TicketNotFoundException;
import org.zalmoxis.evetic.repositories.QrCodeRepo;
import org.zalmoxis.evetic.repositories.TicketRepo;
import org.zalmoxis.evetic.repositories.TicketValidationRepo;
import org.zalmoxis.evetic.services.TicketValidationService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketValidationServiceImpl
        implements TicketValidationService
{

    private final QrCodeRepo qrCodeRepo;
    private final TicketValidationRepo ticketValidationRepo;
    private final TicketRepo ticketRepo;

    @Override
    @Transactional
    public TicketValidation validateTicketByQrCode(UUID qrCodeId, UUID eventId)
    {
        QrCode qrCode = qrCodeRepo.findByIdAndStatusAndEventId(qrCodeId, QrCodeStatusEnum.ACTIVE, eventId)
                .orElseThrow(() -> new QrCodeInvalidException("QR Code with ID " + qrCodeId + " is invalid, inactive, or not for this event."));


        Ticket ticket = qrCode.getTicket();
        TicketValidation ticketValidation = TicketValidation.builder()
                .ticket(ticket)
                .method(TicketValidationMethodEnum.QR_CODE).build();

        TicketValidationStatusEnum status = determineValidationStatus(ticket);

        ticketValidation.setStatus(status);
        ticketValidation.setValidationDate(LocalDateTime.now());



        return ticketValidationRepo.save(ticketValidation);
    }

    @Override
    @Transactional
    public TicketValidation validateTicketManually(UUID ticketId, UUID eventId)
    {
        Ticket ticket = ticketRepo.findByIdAndEventId(ticketId, eventId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket with ID " + ticketId + " not found for this event."));

        TicketValidation ticketValidation = TicketValidation.builder()
                .ticket(ticket)
                .method(TicketValidationMethodEnum.MANUAL_ENTRY).build();

        TicketValidationStatusEnum status = determineValidationStatus(ticket);
        ticketValidation.setStatus(status);
        ticketValidation.setValidationDate(LocalDateTime.now());

        return ticketValidationRepo.save(ticketValidation);
    }

    private TicketValidationStatusEnum determineValidationStatus(Ticket ticket)
    {
        boolean hasValidValidation = ticket.getTicketValidations().stream()
                .anyMatch(tv -> tv.getStatus().equals(TicketValidationStatusEnum.VALID));

        return hasValidValidation ? TicketValidationStatusEnum.INVALID : TicketValidationStatusEnum.VALID;
    }

}
