package org.zalmoxis.evetic.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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

    @Test
    void equals_ShouldReturnTrue_WhenSameObject() {
        QrCode qrCode = QrCode.builder()
                .id(UUID.randomUUID())
                .status(QrCodeStatusEnum.ACTIVE)
                .code("test")
                .build();

        assertEquals(qrCode, qrCode);
    }

    @Test
    void equals_ShouldReturnTrue_WhenEqualObjects() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        QrCode qr1 = QrCode.builder()
                .id(id)
                .status(QrCodeStatusEnum.ACTIVE)
                .code("testcode")
                .createdAt(now)
                .updatedAt(now)
                .build();

        QrCode qr2 = QrCode.builder()
                .id(id)
                .status(QrCodeStatusEnum.ACTIVE)
                .code("testcode")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(qr1, qr2);
        assertEquals(qr1.hashCode(), qr2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        QrCode qr1 = QrCode.builder().id(UUID.randomUUID()).status(QrCodeStatusEnum.ACTIVE).code("test").build();
        QrCode qr2 = QrCode.builder().id(UUID.randomUUID()).status(QrCodeStatusEnum.ACTIVE).code("test").build();

        assertFalse(qr1.equals(qr2));
    }

    @Test
    void equals_ShouldReturnFalse_WhenNull() {
        QrCode qrCode = QrCode.builder().id(UUID.randomUUID()).build();

        assertFalse(qrCode.equals(null));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentClass() {
        QrCode qrCode = QrCode.builder().id(UUID.randomUUID()).build();

        assertFalse(qrCode.equals("string"));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentStatus() {
        UUID id = UUID.randomUUID();
        QrCode qr1 = QrCode.builder().id(id).status(QrCodeStatusEnum.ACTIVE).code("test").build();
        QrCode qr2 = QrCode.builder().id(id).status(QrCodeStatusEnum.INACTIVE).code("test").build();

        assertFalse(qr1.equals(qr2));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentCode() {
        UUID id = UUID.randomUUID();
        QrCode qr1 = QrCode.builder().id(id).status(QrCodeStatusEnum.ACTIVE).code("code1").build();
        QrCode qr2 = QrCode.builder().id(id).status(QrCodeStatusEnum.ACTIVE).code("code2").build();

        assertFalse(qr1.equals(qr2));
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        QrCode qrCode = QrCode.builder()
                .id(UUID.randomUUID())
                .status(QrCodeStatusEnum.ACTIVE)
                .code("test")
                .build();

        int hashCode1 = qrCode.hashCode();
        int hashCode2 = qrCode.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_ShouldBeDifferent_ForDifferentObjects() {
        QrCode qr1 = QrCode.builder().id(UUID.randomUUID()).status(QrCodeStatusEnum.ACTIVE).code("code1").build();
        QrCode qr2 = QrCode.builder().id(UUID.randomUUID()).status(QrCodeStatusEnum.INACTIVE).code("code2").build();

        assertFalse(qr1.hashCode() == qr2.hashCode());
    }
}

