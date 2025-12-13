package org.zalmoxis.evetic.entities;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class QrCodeTest {

    @Test
    void builder_ShouldCreateQrCode() {
        UUID id = UUID.randomUUID();
        String code = "base64encodedimage";
        QrCodeStatusEnum status = QrCodeStatusEnum.ACTIVE;

        QrCode qrCode = QrCode.builder()
                .id(id)
                .code(code)
                .status(status)
                .build();

        assertEquals(id, qrCode.getId());
        assertEquals(code, qrCode.getCode());
        assertEquals(status, qrCode.getStatus());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        QrCode qrCode = new QrCode();
        UUID id = UUID.randomUUID();

        qrCode.setId(id);
        qrCode.setCode("newcode");
        qrCode.setStatus(QrCodeStatusEnum.INACTIVE);

        assertEquals(id, qrCode.getId());
        assertEquals("newcode", qrCode.getCode());
        assertEquals(QrCodeStatusEnum.INACTIVE, qrCode.getStatus());
    }

    @Test
    void ticket_ShouldBeSettable() {
        QrCode qrCode = new QrCode();
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .status(TicketStatusEnum.PURCHASED)
                .build();

        qrCode.setTicket(ticket);

        assertNotNull(qrCode.getTicket());
        assertEquals(TicketStatusEnum.PURCHASED, qrCode.getTicket().getStatus());
    }

    @Test
    void statusEnum_ShouldHaveExpectedValues() {
        assertEquals(2, QrCodeStatusEnum.values().length);
        assertNotNull(QrCodeStatusEnum.valueOf("ACTIVE"));
        assertNotNull(QrCodeStatusEnum.valueOf("INACTIVE"));
    }

    @Test
    void activeQrCode_ShouldBeCreatable() {
        QrCode qrCode = QrCode.builder()
                .id(UUID.randomUUID())
                .code("encodedQrCodeData")
                .status(QrCodeStatusEnum.ACTIVE)
                .build();

        assertEquals(QrCodeStatusEnum.ACTIVE, qrCode.getStatus());
        assertNotNull(qrCode.getCode());
    }

    @Test
    void inactiveQrCode_ShouldBeCreatable() {
        QrCode qrCode = QrCode.builder()
                .status(QrCodeStatusEnum.INACTIVE)
                .build();

        assertEquals(QrCodeStatusEnum.INACTIVE, qrCode.getStatus());
    }
}

