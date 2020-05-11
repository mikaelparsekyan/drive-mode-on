package com.project.drivemodeon.domain.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {
    @NotNull
    @Column
    private String username;

    @NotNull
    @Column
    private String email;

    @NotNull
    @Column
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_relations",
            joinColumns = {
                    @JoinColumn(name = "user")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "following_user")
            }
    )
    private Set<User> followers;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_relations",
            joinColumns = {
                    @JoinColumn(name = "following_user")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user")
            }
    )
    private Set<User> following;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, email, password);
    }
}
