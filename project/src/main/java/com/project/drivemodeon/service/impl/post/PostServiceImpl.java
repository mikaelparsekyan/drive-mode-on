package com.project.drivemodeon.service.impl.post;

import com.project.drivemodeon.model.entity.Post;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.post.PostServiceModel;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.repository.PostRepository;
import com.project.drivemodeon.service.api.post.PostService;
import com.project.drivemodeon.service.api.user.UserService;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, UserService userService, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addPost(PostServiceModel postServiceModel) {
        Post post = modelMapper.map(postServiceModel, Post.class);
        this.postRepository.saveAndFlush(post);
    }

    @Override
    public List<PostServiceModel> getAllPostsByPrivacy(PostPrivacyEnum postPrivacyEnum) {
        return this.postRepository.findAllByPostPrivacyLike(postPrivacyEnum)
                .stream()
                .map(post -> modelMapper.map(post, PostServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PostServiceModel> getAllFeedPostsByUser(UserServiceModel user) {
        User userEntity = userService.getUserByUsername(user.getUsername());
        if (userEntity == null) {
            return null;
        }
        return this.postRepository
                .findAll()
                .stream()
                .filter(post -> {
                    User author = post.getAuthor();
                    if (userEntity.getFollowing().contains(author)) {
                        if (post.getPostPrivacy() != PostPrivacyEnum.ONLY_ME) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(post -> modelMapper.map(post, PostServiceModel.class))
                .collect(Collectors.toList());
    }
}
