package com.project.drivemodeon.service.impl.post;

import com.project.drivemodeon.model.entity.Post;
import com.project.drivemodeon.model.service.post.PostServiceModel;
import com.project.drivemodeon.repository.PostRepository;
import com.project.drivemodeon.service.api.post.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addPost(PostServiceModel postServiceModel) {
        Post post = modelMapper.map(postServiceModel, Post.class);
        this.postRepository.saveAndFlush(post);
    }
}
