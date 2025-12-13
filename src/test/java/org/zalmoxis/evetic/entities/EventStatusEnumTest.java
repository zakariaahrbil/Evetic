package org.zalmoxis.evetic.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventStatusEnumTest {

    @Test
    void shouldHaveExpectedValues() {
        assertEquals(4, EventStatusEnum.values().length);
        assertNotNull(EventStatusEnum.valueOf("DRAFT"));
        assertNotNull(EventStatusEnum.valueOf("PUBLISHED"));
        assertNotNull(EventStatusEnum.valueOf("CANCELLED"));
        assertNotNull(EventStatusEnum.valueOf("COMPLETED"));
    }

    @Test
    void draft_ShouldExist() {
        EventStatusEnum status = EventStatusEnum.DRAFT;
        assertEquals("DRAFT", status.name());
    }

    @Test
    void published_ShouldExist() {
        EventStatusEnum status = EventStatusEnum.PUBLISHED;
        assertEquals("PUBLISHED", status.name());
    }

    @Test
    void cancelled_ShouldExist() {
        EventStatusEnum status = EventStatusEnum.CANCELLED;
        assertEquals("CANCELLED", status.name());
    }

    @Test
    void completed_ShouldExist() {
        EventStatusEnum status = EventStatusEnum.COMPLETED;
        assertEquals("COMPLETED", status.name());
    }
}

