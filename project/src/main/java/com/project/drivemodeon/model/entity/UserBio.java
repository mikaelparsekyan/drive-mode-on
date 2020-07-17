package com.project.drivemodeon.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "bios")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class UserBio extends BaseEntity {
    @Column
    @NonNull
    private String value;

    @OneToOne(mappedBy = "bio", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @Override
    public String toString() {
        return getValue();
    }
}
