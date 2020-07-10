package com.project.drivemodeon.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "comments")
@Data
public class Comment extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @Column(nullable = false)
    private String text;

    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(
                    name = "comment_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "liker_id", referencedColumnName = "id"
            )
    )
    private Set<User> likers;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;
}
