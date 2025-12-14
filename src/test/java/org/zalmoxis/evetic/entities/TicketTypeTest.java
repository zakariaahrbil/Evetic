package org.zalmoxis.evetic.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TicketTypeTest {

    @Test
    void builder_ShouldCreateTicketType() {
        UUID id = UUID.randomUUID();
        String name = "VIP";
        String description = "VIP Access";
        Double price = 100.0;
        Integer totalAvailable = 50;

        TicketType ticketType = TicketType.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .totalAvailable(totalAvailable)
                .build();

        assertEquals(id, ticketType.getId());
        assertEquals(name, ticketType.getName());
        assertEquals(description, ticketType.getDescription());
        assertEquals(price, ticketType.getPrice());
        assertEquals(totalAvailable, ticketType.getTotalAvailable());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        TicketType ticketType = new TicketType();
        UUID id = UUID.randomUUID();

        ticketType.setId(id);
        ticketType.setName("General Admission");
        ticketType.setDescription("Standard entry");
        ticketType.setPrice(50.0);
        ticketType.setTotalAvailable(100);

        assertEquals(id, ticketType.getId());
        assertEquals("General Admission", ticketType.getName());
        assertEquals("Standard entry", ticketType.getDescription());
        assertEquals(50.0, ticketType.getPrice());
        assertEquals(100, ticketType.getTotalAvailable());
    }

    @Test
    void event_ShouldBeSettable() {
        TicketType ticketType = new TicketType();
        Event event = Event.builder()
                .id(UUID.randomUUID())
                .name("Concert")
                .build();

        ticketType.setEvent(event);

        assertNotNull(ticketType.getEvent());
        assertEquals("Concert", ticketType.getEvent().getName());
    }

    @Test
    void tickets_ShouldBeSettable() {
        TicketType ticketType = new TicketType();
        Ticket ticket = Ticket.builder()
                .id(UUID.randomUUID())
                .status(TicketStatusEnum.PURCHASED)
                .build();

        ticketType.setTickets(new ArrayList<>());
        ticketType.getTickets().add(ticket);

        assertEquals(1, ticketType.getTickets().size());
    }

    @Test
    void price_ShouldAcceptZero() {
        TicketType ticketType = TicketType.builder()
                .name("Free Entry")
                .price(0.0)
                .build();

        assertEquals(0.0, ticketType.getPrice());
    }

    @Test
    void totalAvailable_ShouldBeNullable() {
        TicketType ticketType = TicketType.builder()
                .name("Unlimited")
                .price(25.0)
                .totalAvailable(null)
                .build();

        assertNull(ticketType.getTotalAvailable());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameObject() {
        TicketType ticketType = TicketType.builder()
                .id(UUID.randomUUID())
                .name("VIP")
                .build();

        assertEquals(ticketType, ticketType);
    }

    @Test
    void equals_ShouldReturnTrue_WhenEqualObjects() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        TicketType type1 = TicketType.builder()
                .id(id)
                .name("VIP")
                .description("VIP Access")
                .price(100.0)
                .totalAvailable(50)
                .createdAt(now)
                .updatedAt(now)
                .build();

        TicketType type2 = TicketType.builder()
                .id(id)
                .name("VIP")
                .description("VIP Access")
                .price(100.0)
                .totalAvailable(50)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(type1, type2);
        assertEquals(type1.hashCode(), type2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        TicketType type1 = TicketType.builder().id(UUID.randomUUID()).name("VIP").build();
        TicketType type2 = TicketType.builder().id(UUID.randomUUID()).name("VIP").build();

        assertFalse(type1.equals(type2));
    }

    @Test
    void equals_ShouldReturnFalse_WhenNull() {
        TicketType ticketType = TicketType.builder().id(UUID.randomUUID()).build();

        assertFalse(ticketType.equals(null));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentClass() {
        TicketType ticketType = TicketType.builder().id(UUID.randomUUID()).build();

        assertFalse(ticketType.equals("string"));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentName() {
        UUID id = UUID.randomUUID();
        TicketType type1 = TicketType.builder().id(id).name("VIP").build();
        TicketType type2 = TicketType.builder().id(id).name("General").build();

        assertFalse(type1.equals(type2));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentPrice() {
        UUID id = UUID.randomUUID();
        TicketType type1 = TicketType.builder().id(id).name("VIP").price(100.0).build();
        TicketType type2 = TicketType.builder().id(id).name("VIP").price(200.0).build();

        assertFalse(type1.equals(type2));
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        TicketType ticketType = TicketType.builder()
                .id(UUID.randomUUID())
                .name("VIP")
                .price(100.0)
                .build();

        int hashCode1 = ticketType.hashCode();
        int hashCode2 = ticketType.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_ShouldBeDifferent_ForDifferentObjects() {
        TicketType type1 = TicketType.builder().id(UUID.randomUUID()).name("VIP").build();
        TicketType type2 = TicketType.builder().id(UUID.randomUUID()).name("General").build();

        assertFalse(type1.hashCode() == type2.hashCode());
    }
}

