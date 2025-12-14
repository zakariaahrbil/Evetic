package org.zalmoxis.evetic.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketValidationTest {

    @Test
    void builder_ShouldCreateTicketValidation() {
        UUID id = UUID.randomUUID();
        TicketValidationStatusEnum status = TicketValidationStatusEnum.VALID;
        TicketValidationMethodEnum method = TicketValidationMethodEnum.QR_CODE;
        LocalDateTime validationDate = LocalDateTime.now();

        TicketValidation validation = TicketValidation.builder()
                .id(id)
                .status(status)
                .method(method)
                .validationDate(validationDate)
                .build();

        assertEquals(id, validation.getId());
        assertEquals(status, validation.getStatus());
        assertEquals(method, validation.getMethod());
        assertEquals(validationDate, validation.getValidationDate());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        TicketValidation validation = new TicketValidation();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        validation.setId(id);
        validation.setStatus(TicketValidationStatusEnum.INVALID);
        validation.setMethod(TicketValidationMethodEnum.MANUAL_ENTRY);
        validation.setValidationDate(now);

        assertEquals(id, validation.getId());
        assertEquals(TicketValidationStatusEnum.INVALID, validation.getStatus());
        assertEquals(TicketValidationMethodEnum.MANUAL_ENTRY, validation.getMethod());
        assertEquals(now, validation.getValidationDate());
    }

    @Test
    void ticket_ShouldBeSettable() {
        TicketValidation validation = new TicketValidation();
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .status(TicketStatusEnum.PURCHASED)
                .build();

        validation.setTicket(ticket);

        assertNotNull(validation.getTicket());
        assertEquals(TicketStatusEnum.PURCHASED, validation.getTicket().getStatus());
    }

    @Test
    void validationStatusEnum_ShouldHaveExpectedValues() {
        assertEquals(3, TicketValidationStatusEnum.values().length);
        assertNotNull(TicketValidationStatusEnum.valueOf("VALID"));
        assertNotNull(TicketValidationStatusEnum.valueOf("INVALID"));
        assertNotNull(TicketValidationStatusEnum.valueOf("EXPIRED"));
    }

    @Test
    void validationMethodEnum_ShouldHaveExpectedValues() {
        assertEquals(2, TicketValidationMethodEnum.values().length);
        assertNotNull(TicketValidationMethodEnum.valueOf("QR_CODE"));
        assertNotNull(TicketValidationMethodEnum.valueOf("MANUAL_ENTRY"));
    }

    @Test
    void qrCodeValidation_ShouldBeCreatable() {
        TicketValidation validation = TicketValidation.builder()
                .method(TicketValidationMethodEnum.QR_CODE)
                .status(TicketValidationStatusEnum.VALID)
                .validationDate(LocalDateTime.now())
                .build();

        assertEquals(TicketValidationMethodEnum.QR_CODE, validation.getMethod());
        assertEquals(TicketValidationStatusEnum.VALID, validation.getStatus());
    }

    @Test
    void manualValidation_ShouldBeCreatable() {
        TicketValidation validation = TicketValidation.builder()
                .method(TicketValidationMethodEnum.MANUAL_ENTRY)
                .status(TicketValidationStatusEnum.VALID)
                .validationDate(LocalDateTime.now())
                .build();

        assertEquals(TicketValidationMethodEnum.MANUAL_ENTRY, validation.getMethod());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameObject() {
        TicketValidation validation = TicketValidation.builder()
                .id(UUID.randomUUID())
                .status(TicketValidationStatusEnum.VALID)
                .build();

        assertEquals(validation, validation);
    }

    @Test
    void equals_ShouldReturnTrue_WhenEqualObjects() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        TicketValidation validation1 = TicketValidation.builder()
                .id(id)
                .status(TicketValidationStatusEnum.VALID)
                .validationDate(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        TicketValidation validation2 = TicketValidation.builder()
                .id(id)
                .status(TicketValidationStatusEnum.VALID)
                .validationDate(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(validation1, validation2);
        assertEquals(validation1.hashCode(), validation2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        TicketValidation v1 = TicketValidation.builder().id(UUID.randomUUID()).status(TicketValidationStatusEnum.VALID).build();
        TicketValidation v2 = TicketValidation.builder().id(UUID.randomUUID()).status(TicketValidationStatusEnum.VALID).build();

        assertFalse(v1.equals(v2));
    }

    @Test
    void equals_ShouldReturnFalse_WhenNull() {
        TicketValidation validation = TicketValidation.builder().id(UUID.randomUUID()).build();

        assertFalse(validation.equals(null));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentClass() {
        TicketValidation validation = TicketValidation.builder().id(UUID.randomUUID()).build();

        assertFalse(validation.equals("string"));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentStatus() {
        UUID id = UUID.randomUUID();
        TicketValidation v1 = TicketValidation.builder().id(id).status(TicketValidationStatusEnum.VALID).build();
        TicketValidation v2 = TicketValidation.builder().id(id).status(TicketValidationStatusEnum.INVALID).build();

        assertFalse(v1.equals(v2));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentValidationDate() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        TicketValidation v1 = TicketValidation.builder().id(id).validationDate(now).build();
        TicketValidation v2 = TicketValidation.builder().id(id).validationDate(now.plusHours(1)).build();

        assertFalse(v1.equals(v2));
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        TicketValidation validation = TicketValidation.builder()
                .id(UUID.randomUUID())
                .status(TicketValidationStatusEnum.VALID)
                .build();

        int hashCode1 = validation.hashCode();
        int hashCode2 = validation.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_ShouldBeDifferent_ForDifferentObjects() {
        TicketValidation v1 = TicketValidation.builder().id(UUID.randomUUID()).status(TicketValidationStatusEnum.VALID).build();
        TicketValidation v2 = TicketValidation.builder().id(UUID.randomUUID()).status(TicketValidationStatusEnum.INVALID).build();

        assertFalse(v1.hashCode() == v2.hashCode());
    }
}

