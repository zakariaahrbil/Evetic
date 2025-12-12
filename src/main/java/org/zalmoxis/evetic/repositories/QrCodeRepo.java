package org.zalmoxis.evetic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zalmoxis.evetic.entities.QrCode;

import java.util.UUID;

public interface QrCodeRepo
        extends JpaRepository<QrCode, UUID>
{

}
