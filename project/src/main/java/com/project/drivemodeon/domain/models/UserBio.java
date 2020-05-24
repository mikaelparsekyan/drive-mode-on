package com.project.drivemodeon.domain.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "bios")
@Data
public class UserBio extends BaseEntity {
    @Column
    private String value;

    @OneToOne(mappedBy = "bio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
}
