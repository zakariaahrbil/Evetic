package org.zalmoxis.evetic.services;

import org.zalmoxis.evetic.entities.TicketValidation;

import java.util.UUID;

public interface TicketValidationService
{
    TicketValidation validateTicketByQrCode(UUID qrCodeId,UUID eventId);
    TicketValidation validateTicketManually(UUID ticketId,UUID eventId);
}
