package org.zalmoxis.evetic.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    void shouldHaveExpectedValues() {
        assertEquals(3, UserRole.values().length);
        assertNotNull(UserRole.valueOf("ATTENDEE"));
        assertNotNull(UserRole.valueOf("ORGANIZER"));
        assertNotNull(UserRole.valueOf("STAFF"));
    }

    @Test
    void attendee_ShouldExist() {
        UserRole role = UserRole.ATTENDEE;
        assertEquals("ATTENDEE", role.name());
    }

    @Test
    void organizer_ShouldExist() {
        UserRole role = UserRole.ORGANIZER;
        assertEquals("ORGANIZER", role.name());
    }

    @Test
    void staff_ShouldExist() {
        UserRole role = UserRole.STAFF;
        assertEquals("STAFF", role.name());
    }

    @Test
    void getSpringSecurityRole_ShouldReturnRolePrefixedName() {
        UserRole attendee = UserRole.ATTENDEE;
        UserRole organizer = UserRole.ORGANIZER;
        UserRole staff = UserRole.STAFF;

        assertEquals("ROLE_ATTENDEE", attendee.getSpringSecurityRole());
        assertEquals("ROLE_ORGANIZER", organizer.getSpringSecurityRole());
        assertEquals("ROLE_STAFF", staff.getSpringSecurityRole());
    }

    @Test
    void springSecurityRole_ShouldStartWithRolePrefix() {
        for (UserRole role : UserRole.values()) {
            assertTrue(role.getSpringSecurityRole().startsWith("ROLE_"));
        }
    }
}
