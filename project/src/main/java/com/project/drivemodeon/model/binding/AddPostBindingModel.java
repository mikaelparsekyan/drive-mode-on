package com.project.drivemodeon.model.binding;

import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AddPostBindingModel {
    @Length(min = 1, max = 1000)
    private String text;

    private PostPrivacyEnum postPrivacy;
}
