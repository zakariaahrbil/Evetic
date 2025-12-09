package org.zalmoxis.evetic.entities;

public enum UserRole {
    ORGANIZER("ROLE_ORGANIZER"),
    STAFF("ROLE_STAFF"),
    ATTENDEE("ROLE_ATTENDEE");

    private final String springSecurityRole;

    UserRole(String springSecurityRole) {
        this.springSecurityRole = springSecurityRole;
    }

    public String getSpringSecurityRole() {
        return springSecurityRole;
    }
}