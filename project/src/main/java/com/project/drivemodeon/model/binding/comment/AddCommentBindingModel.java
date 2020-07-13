package com.project.drivemodeon.model.binding.comment;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AddCommentBindingModel {

    @Length(min = 1, max = 100, message = "The comment should be between 1 and 100 symbols!")
    private String text;

    private long postId;
}
