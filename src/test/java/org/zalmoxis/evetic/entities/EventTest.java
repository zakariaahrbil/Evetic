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

    @Test
    void equals_ShouldReturnTrue_WhenSameObject() {
        Event event = Event.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .build();

        assertEquals(event, event);
    }

    @Test
    void equals_ShouldReturnTrue_WhenEqualObjects() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Event event1 = Event.builder()
                .id(id)
                .name("Test Event")
                .location("Location")
                .description("Description")
                .startTime(now)
                .endTime(now.plusHours(2))
                .startSalesAt(now.minusDays(1))
                .endSalesAt(now.minusHours(1))
                .status(EventStatusEnum.PUBLISHED)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Event event2 = Event.builder()
                .id(id)
                .name("Test Event")
                .location("Location")
                .description("Description")
                .startTime(now)
                .endTime(now.plusHours(2))
                .startSalesAt(now.minusDays(1))
                .endSalesAt(now.minusHours(1))
                .status(EventStatusEnum.PUBLISHED)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentId() {
        Event event1 = Event.builder().id(UUID.randomUUID()).name("Test").build();
        Event event2 = Event.builder().id(UUID.randomUUID()).name("Test").build();

        assertFalse(event1.equals(event2));
    }

    @Test
    void equals_ShouldReturnFalse_WhenNull() {
        Event event = Event.builder().id(UUID.randomUUID()).build();

        assertFalse(event.equals(null));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentClass() {
        Event event = Event.builder().id(UUID.randomUUID()).build();

        assertFalse(event.equals("string"));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentName() {
        UUID id = UUID.randomUUID();
        Event event1 = Event.builder().id(id).name("Name1").build();
        Event event2 = Event.builder().id(id).name("Name2").build();

        assertFalse(event1.equals(event2));
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentStatus() {
        UUID id = UUID.randomUUID();
        Event event1 = Event.builder().id(id).status(EventStatusEnum.DRAFT).build();
        Event event2 = Event.builder().id(id).status(EventStatusEnum.PUBLISHED).build();

        assertFalse(event1.equals(event2));
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        Event event = Event.builder()
                .id(UUID.randomUUID())
                .name("Test")
                .status(EventStatusEnum.DRAFT)
                .build();

        int hashCode1 = event.hashCode();
        int hashCode2 = event.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_ShouldBeDifferent_ForDifferentObjects() {
        Event event1 = Event.builder().id(UUID.randomUUID()).name("Test1").build();
        Event event2 = Event.builder().id(UUID.randomUUID()).name("Test2").build();

        assertFalse(event1.hashCode() == event2.hashCode());
    }
}

