package com.project.drivemodeon.service.api.post;

import com.project.drivemodeon.model.service.post.PostServiceModel;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public interface PostService {
    void addPost(PostServiceModel postServiceModel);

    List<PostServiceModel> getAllPostsByPrivacy(PostPrivacyEnum postPrivacyEnum);

    LinkedList<PostServiceModel> getAllFeedPostsByUser(UserServiceModel userServiceModel);

    LinkedList<PostServiceModel> getAllDraftsByUser(UserServiceModel userServiceModel);

    Optional<PostServiceModel> getPostById(Long id);

    void likePost(Long postId, String username);

    void dislikePost(Long postId, String username);
}
