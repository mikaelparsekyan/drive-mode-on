package com.project.drivemodeon.model.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true)
    @NonNull
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "bio", referencedColumnName = "id")
    private UserBio bio;

    @NotNull
    @Column
    private String email;

    @NotNull
    @Column
    private String password;

    @Column(name = "image_path")
    private String imagePath;

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
    //TODO rename to 'followings'!
    private Set<User> following;

    @Column(name = "is_account_private")
    private boolean isAccountPrivate;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Post> posts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Log> logs;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private List<AuthorityEntity> authorities = new ArrayList<>();

    @ManyToMany(mappedBy = "likers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Post> likers;

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

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
