package com.calendar.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name = "USERS")
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String first_name;

    @Column
    private String last_name;

    @Column
    private String pass;

    @Column
    private String position;

    @Column
    private String city;

    @Column
    private String phone;

    @Column
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_participants",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id")
    )
    @JsonIgnore
    private Set<Event> events;

    public User(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(pass, user.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, pass);
    }
}
