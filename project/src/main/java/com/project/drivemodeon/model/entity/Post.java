package com.project.drivemodeon.model.entity;

import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
public class Post extends BaseEntity {
    @Size(min = 1, max = 1000)
    @Column
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_privacy")
    private PostPrivacyEnum postPrivacy;

    @Column(name = "is_draft", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isDraft;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author;

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(
                    name = "post_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id"
            )
    )
    private Set<User> likers;

    @Column(name = "posted_on")
    private LocalDateTime postedOn;

    @Column
    private String location;

//    @OneToMany
//    @Column
//    private Set<Picture> postPictures;
}
