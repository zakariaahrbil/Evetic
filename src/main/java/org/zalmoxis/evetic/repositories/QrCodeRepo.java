package org.zalmoxis.evetic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zalmoxis.evetic.entities.QrCode;
import org.zalmoxis.evetic.entities.QrCodeStatusEnum;

import java.util.Optional;
import java.util.UUID;

public interface QrCodeRepo
        extends JpaRepository<QrCode, UUID>
{
    Optional<QrCode> findByTicketIdAndTicketOwnerId(UUID ticketId, UUID ownerId);

    Optional<QrCode> findByIdAndStatus(UUID qrCodeId, QrCodeStatusEnum status);

    @Query("SELECT qc FROM QrCode qc JOIN qc.ticket t JOIN t.ticketType tt WHERE qc.id = :qrCodeId AND qc.status = :status AND tt.event.id = :eventId")
    Optional<QrCode> findByIdAndStatusAndEventId(UUID qrCodeId, QrCodeStatusEnum status, UUID eventId);
}
