package com.project.drivemodeon.model.binding.comment;

import lombok.Data;

@Data
public class AddCommentBindingModel {
    private String text;

    private long postId;
}
