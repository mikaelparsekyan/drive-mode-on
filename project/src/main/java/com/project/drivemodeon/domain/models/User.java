package com.project.drivemodeon.domain.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity {
    @NotNull
    @Column
    private String nickname;

    @NotNull
    @Column
    private String email;

    @NotNull
    @Column
    private String password;
}
