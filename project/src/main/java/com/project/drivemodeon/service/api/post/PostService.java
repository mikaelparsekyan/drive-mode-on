package com.project.drivemodeon.service.api.post;

import com.project.drivemodeon.model.service.post.PostServiceModel;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    void addPost(PostServiceModel postServiceModel);
}
