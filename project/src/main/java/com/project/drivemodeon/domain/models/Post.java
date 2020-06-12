package com.project.drivemodeon.domain.models;

import com.project.drivemodeon.validation.constants.enums.PostPrivacyEnum;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

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

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "id")
    private User author;

//    @OneToMany
//    @Column
//    private Set<Picture> postPictures;
}
