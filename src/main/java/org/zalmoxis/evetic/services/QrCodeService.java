package org.zalmoxis.evetic.services;

import org.zalmoxis.evetic.entities.QrCode;
import org.zalmoxis.evetic.entities.Ticket;

public interface QrCodeService
{
    QrCode generateQrCode(Ticket ticket);
}
