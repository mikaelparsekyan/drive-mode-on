package com.project.drivemodeon.service.api.comment;

import com.project.drivemodeon.model.service.comment.CommentServiceModel;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    void addComment(CommentServiceModel commentServiceModel, long postId);
}
