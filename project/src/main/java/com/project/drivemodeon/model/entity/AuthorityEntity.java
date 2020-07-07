package com.project.drivemodeon.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="authorities")
@Data
public class AuthorityEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
}