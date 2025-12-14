package org.zalmoxis.evetic.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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

    @Test
    void equals_ShouldReturnTrue_WhenSameObject() {
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .status(TicketStatusEnum.PURCHASED)
                .build();

        assertEquals(ticket, ticket);
    }

    @Test
    void equals_ShouldReturnTrue_WhenEqualObjects() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Ticket ticket1 = Ticket.builder()
                .id(id)
                .status(TicketStatusEnum.PURCHASED)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Ticket ticket2 = Ticket.builder()
                .id(id)
                .status(TicketStatusEnum.PURCHASED)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(ticket1, ticket2);
        assertEquals(ticket1.hashCode(), ticket2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        Ticket ticket1 = Ticket.builder().id(UUID.randomUUID()).status(TicketStatusEnum.PURCHASED).build();
        Ticket ticket2 = Ticket.builder().id(UUID.randomUUID()).status(TicketStatusEnum.PURCHASED).build();

        assertFalse(ticket1.equals(ticket2));
    }

    @Test
    void equals_ShouldReturnFalse_WhenNull() {
        Ticket ticket = Ticket.builder().id(UUID.randomUUID()).build();

        assertFalse(ticket.equals(null));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentClass() {
        Ticket ticket = Ticket.builder().id(UUID.randomUUID()).build();

        assertFalse(ticket.equals("string"));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentStatus() {
        UUID id = UUID.randomUUID();
        Ticket ticket1 = Ticket.builder().id(id).status(TicketStatusEnum.PURCHASED).build();
        Ticket ticket2 = Ticket.builder().id(id).status(TicketStatusEnum.CANCELLED).build();

        assertFalse(ticket1.equals(ticket2));
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .status(TicketStatusEnum.PURCHASED)
                .build();

        int hashCode1 = ticket.hashCode();
        int hashCode2 = ticket.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_ShouldBeDifferent_ForDifferentObjects() {
        Ticket ticket1 = Ticket.builder().id(UUID.randomUUID()).status(TicketStatusEnum.PURCHASED).build();
        Ticket ticket2 = Ticket.builder().id(UUID.randomUUID()).status(TicketStatusEnum.CANCELLED).build();

        assertFalse(ticket1.hashCode() == ticket2.hashCode());
    }
}
