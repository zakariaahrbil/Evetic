package org.zalmoxis.evetic.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void builder_ShouldCreateEvent() {
        UUID id = UUID.randomUUID();
        String name = "Test Event";
        String location = "Test Location";
        String description = "Test Description";
        LocalDateTime startTime = LocalDateTime.now().plusDays(1);
        LocalDateTime endTime = LocalDateTime.now().plusDays(2);
        EventStatusEnum status = EventStatusEnum.DRAFT;

        Event event = Event.builder()
                .id(id)
                .name(name)
                .location(location)
                .description(description)
                .startTime(startTime)
                .endTime(endTime)
                .status(status)
                .build();

        assertEquals(id, event.getId());
        assertEquals(name, event.getName());
        assertEquals(location, event.getLocation());
        assertEquals(description, event.getDescription());
        assertEquals(startTime, event.getStartTime());
        assertEquals(endTime, event.getEndTime());
        assertEquals(status, event.getStatus());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        Event event = new Event();
        UUID id = UUID.randomUUID();
        String name = "Updated Event";

        event.setId(id);
        event.setName(name);
        event.setLocation("Updated Location");
        event.setStatus(EventStatusEnum.PUBLISHED);

        assertEquals(id, event.getId());
        assertEquals(name, event.getName());
        assertEquals("Updated Location", event.getLocation());
        assertEquals(EventStatusEnum.PUBLISHED, event.getStatus());
    }

    @Test
    void ticketTypes_ShouldBeSettable() {
        Event event = new Event();
        TicketType ticketType = TicketType.builder()
                .name("VIP")
                .price(100.0)
                .build();

        event.setTicketTypes(new ArrayList<>());
        event.getTicketTypes().add(ticketType);

        assertEquals(1, event.getTicketTypes().size());
        assertEquals("VIP", event.getTicketTypes().get(0).getName());
    }

    @Test
    void organizer_ShouldBeSettable() {
        Event event = new Event();
        User organizer = User.builder()
                .id(UUID.randomUUID())
                .username("organizer")
                .build();

        event.setOrganizer(organizer);

        assertNotNull(event.getOrganizer());
        assertEquals("organizer", event.getOrganizer().getUsername());
    }

    @Test
    void salesDates_ShouldBeSettable() {
        Event event = new Event();
        LocalDateTime startSales = LocalDateTime.now();
        LocalDateTime endSales = LocalDateTime.now().plusDays(7);

        event.setStartSalesAt(startSales);
        event.setEndSalesAt(endSales);

        assertEquals(startSales, event.getStartSalesAt());
        assertEquals(endSales, event.getEndSalesAt());
    }
}

