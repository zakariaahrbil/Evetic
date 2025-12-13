package org.zalmoxis.evetic.services;

import org.zalmoxis.evetic.entities.QrCode;
import org.zalmoxis.evetic.entities.Ticket;

import java.util.UUID;

public interface QrCodeService
{
    QrCode generateQrCode(Ticket ticket);
    byte[] getQrCodeImageForOwner(UUID ticketId, UUID ownerId);
}
