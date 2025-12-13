package org.zalmoxis.evetic.entities;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void builder_ShouldCreateUser() {
        UUID id = UUID.randomUUID();
        String username = "testuser";
        String email = "test@test.com";
        String password = "password123";

        User user = User.builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .build();

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        User user = new User();
        UUID id = UUID.randomUUID();

        user.setId(id);
        user.setUsername("newuser");
        user.setEmail("new@test.com");
        user.setPassword("newpassword");

        assertEquals(id, user.getId());
        assertEquals("newuser", user.getUsername());
        assertEquals("new@test.com", user.getEmail());
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void roles_ShouldBeSettable() {
        User user = new User();
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.ATTENDEE);
        roles.add(UserRole.ORGANIZER);

        user.setRoles(roles);

        assertNotNull(user.getRoles());
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(UserRole.ATTENDEE));
        assertTrue(user.getRoles().contains(UserRole.ORGANIZER));
    }

    @Test
    void roles_ShouldContainSingleRole() {
        User user = User.builder()
                .roles(Set.of(UserRole.STAFF))
                .build();

        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(UserRole.STAFF));
    }

    @Test
    void events_ShouldBeSettable() {
        User user = new User();
        Event event = Event.builder()
                .name("Test Event")
                .build();

        user.setEvents(new java.util.ArrayList<>());
        user.getEvents().add(event);

        assertEquals(1, user.getEvents().size());
    }

    @Test
    void attendingEvents_ShouldBeSettable() {
        User user = new User();
        Event event = Event.builder()
                .name("Attending Event")
                .build();

        user.setAttendingEvents(new java.util.ArrayList<>());
        user.getAttendingEvents().add(event);

        assertEquals(1, user.getAttendingEvents().size());
    }
}

