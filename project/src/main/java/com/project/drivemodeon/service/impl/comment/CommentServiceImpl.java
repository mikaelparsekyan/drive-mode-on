package com.project.drivemodeon.service.impl.comment;

import com.project.drivemodeon.model.entity.Comment;
import com.project.drivemodeon.model.entity.Post;
import com.project.drivemodeon.model.service.comment.CommentServiceModel;
import com.project.drivemodeon.model.service.post.PostServiceModel;
import com.project.drivemodeon.repository.CommentRepository;
import com.project.drivemodeon.service.api.comment.CommentService;
import com.project.drivemodeon.service.api.post.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final ModelMapper modelMapper;
    private final PostService postService;

    private final CommentRepository commentRepository;

    public CommentServiceImpl(ModelMapper modelMapper, PostService postService, CommentRepository commentRepository) {
        this.modelMapper = modelMapper;
        this.postService = postService;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public void addComment(CommentServiceModel commentServiceModel, long postId) {
        Comment comment = modelMapper.map(commentServiceModel, Comment.class);

        Optional<PostServiceModel> postById = postService.getPostById(postId);
        if (postById.isPresent()) {
            Post post = modelMapper.map(postById.get(), Post.class);
            comment.setPost(post);
            commentRepository.saveAndFlush(comment);
        }
    }
}
