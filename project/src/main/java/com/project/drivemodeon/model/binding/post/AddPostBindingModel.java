package com.project.drivemodeon.model.binding.post;

import com.project.drivemodeon.model.entity.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class AddPostBindingModel {
    @Length(min = 1, max = 1000)
    private String value;

    @NotNull
    private String postPrivacy;

    @NotNull
    private int isDraft;

    private String location;
}
