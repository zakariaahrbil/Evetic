package org.zalmoxis.evetic.entities;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void builder_ShouldCreateTicket() {
        UUID id = UUID.randomUUID();
        TicketStatusEnum status = TicketStatusEnum.PURCHASED;

        Ticket ticket = Ticket.builder()
                .id(id)
                .status(status)
                .build();

        assertEquals(id, ticket.getId());
        assertEquals(status, ticket.getStatus());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        Ticket ticket = new Ticket();
        UUID id = UUID.randomUUID();

        ticket.setId(id);
        ticket.setStatus(TicketStatusEnum.CANCELLED);

        assertEquals(id, ticket.getId());
        assertEquals(TicketStatusEnum.CANCELLED, ticket.getStatus());
    }

    @Test
    void owner_ShouldBeSettable() {
        Ticket ticket = new Ticket();
        User owner = User.builder()
                .id(UUID.randomUUID())
                .username("ticketowner")
                .build();

        ticket.setOwner(owner);

        assertNotNull(ticket.getOwner());
        assertEquals("ticketowner", ticket.getOwner().getUsername());
    }

    @Test
    void ticketType_ShouldBeSettable() {
        Ticket ticket = new Ticket();
        TicketType ticketType = TicketType.builder()
                .id(UUID.randomUUID())
                .name("VIP")
                .price(150.0)
                .build();

        ticket.setTicketType(ticketType);

        assertNotNull(ticket.getTicketType());
        assertEquals("VIP", ticket.getTicketType().getName());
        assertEquals(150.0, ticket.getTicketType().getPrice());
    }

    @Test
    void ticketValidations_ShouldBeSettable() {
        Ticket ticket = new Ticket();
        TicketValidation validation = TicketValidation.builder()
                .status(TicketValidationStatusEnum.VALID)
                .build();

        ticket.setTicketValidations(new ArrayList<>());
        ticket.getTicketValidations().add(validation);

        assertEquals(1, ticket.getTicketValidations().size());
        assertEquals(TicketValidationStatusEnum.VALID, ticket.getTicketValidations().get(0).getStatus());
    }

    @Test
    void codes_ShouldBeSettable() {
        Ticket ticket = new Ticket();
        QrCode qrCode = QrCode.builder()
                .id(UUID.randomUUID())
                .status(QrCodeStatusEnum.ACTIVE)
                .build();

        ticket.setCodes(new ArrayList<>());
        ticket.getCodes().add(qrCode);

        assertEquals(1, ticket.getCodes().size());
        assertEquals(QrCodeStatusEnum.ACTIVE, ticket.getCodes().get(0).getStatus());
    }

    @Test
    void statusEnum_ShouldHaveExpectedValues() {
        assertEquals(2, TicketStatusEnum.values().length);
        assertNotNull(TicketStatusEnum.valueOf("PURCHASED"));
        assertNotNull(TicketStatusEnum.valueOf("CANCELLED"));
    }
}

