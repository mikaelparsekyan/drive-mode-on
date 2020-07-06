package com.project.drivemodeon.service.api.post;

import com.project.drivemodeon.model.service.post.PostServiceModel;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {
    void addPost(PostServiceModel postServiceModel);

    List<PostServiceModel> getAllPostsByPrivacy(PostPrivacyEnum postPrivacyEnum);

    List<PostServiceModel> getAllFeedPostsByUser(UserServiceModel userServiceModel);
}
