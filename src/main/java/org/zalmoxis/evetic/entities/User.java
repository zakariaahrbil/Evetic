package org.zalmoxis.evetic.entities;

        import jakarta.persistence.Column;
        import jakarta.persistence.ElementCollection;
        import jakarta.persistence.Entity;
        import jakarta.persistence.EnumType;
        import jakarta.persistence.Enumerated;
        import jakarta.persistence.FetchType;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Id;
        import jakarta.persistence.JoinColumn;
        import jakarta.persistence.JoinTable;
        import jakarta.persistence.ManyToMany;
        import jakarta.persistence.OneToMany;
        import jakarta.persistence.Table;
        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Getter;
        import lombok.NoArgsConstructor;
        import lombok.Setter;
        import org.springframework.data.annotation.CreatedDate;
        import org.springframework.data.annotation.LastModifiedDate;

        import java.util.ArrayList;
        import java.util.HashSet;
        import java.util.List;
        import java.util.Objects;
        import java.util.Set;
        import java.util.UUID;

        @Entity
        @Table(name = "users")
        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public class User {
            @Id
            @Column(name = "id", nullable = false, unique = true, updatable = false)
            @GeneratedValue(strategy = GenerationType.UUID)
            private UUID id;

            @Column(name = "username", nullable = false, unique = true)
            private String username;

            @Column(name = "email", nullable = false, unique = true)
            private String email;

            @Column(name = "password", nullable = false)
            private String password;

            @ElementCollection(fetch = FetchType.EAGER)
            @Enumerated(EnumType.STRING)
            @Column(name = "role")
            private Set<UserRole> roles = new HashSet<>();

            @CreatedDate
            @Column(name = "created_at", nullable = false, updatable = false)
            private Long createdAt;

            @LastModifiedDate
            @Column(name = "updated_at", nullable = false)
            private Long updatedAt;

            @OneToMany(mappedBy = "organizer", fetch = FetchType.LAZY)
            private List<Event> events = new ArrayList<>();

            @ManyToMany(fetch = FetchType.LAZY)
            @JoinTable(
                    name = "user_attending_events",
                    joinColumns = @JoinColumn(name = "user_id"),
                    inverseJoinColumns = @JoinColumn(name = "event_id")
            )
            private List<Event> attendingEvents = new ArrayList<>();

            @ManyToMany(fetch = FetchType.LAZY)
            @JoinTable(
                    name = "user_staffing_events",
                    joinColumns = @JoinColumn(name = "user_id"),
                    inverseJoinColumns = @JoinColumn(name = "event_id")
            )
            private List<Event> staffingEvents = new ArrayList<>();

            @Override
            public boolean equals(Object o) {
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                User user = (User) o;
                return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email);
            }

            @Override
            public int hashCode() {
                return Objects.hash(id, username, email);
            }
        }