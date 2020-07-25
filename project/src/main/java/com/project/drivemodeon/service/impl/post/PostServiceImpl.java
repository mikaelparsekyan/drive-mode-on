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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
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
    public LinkedList<PostServiceModel> getAllPostsByPrivacy(PostPrivacyEnum postPrivacyEnum) {
        return this.postRepository
                .findAllByPostPrivacyLikeOrderByPostedOnDesc(postPrivacyEnum)
                .stream()
                .map(post -> modelMapper.map(post, PostServiceModel.class))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public LinkedList<PostServiceModel> getAllFeedPostsByUser(UserServiceModel user) {
        User userEntity = userService.getUserByUsername(user.getUsername());
        if (userEntity == null) {
            return null;
        }
        return this.postRepository
                .findAllByDraftIsFalse()
                .stream()
                .filter(post -> {
                    User author = post.getAuthor();
                    if (userEntity.getFollowing().contains(author) ||
                            author.getId() == user.getId()) {
                        if (post.getPostPrivacy() != PostPrivacyEnum.ONLY_ME) {
                            return true;
                        }
                    }
                    return false;
                })
                .sorted((p1, p2) -> p2.getPostedOn().compareTo(p1.getPostedOn()))
                .map(post -> modelMapper.map(post, PostServiceModel.class))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public LinkedList<PostServiceModel> getAllDraftsByUser(UserServiceModel user) {
        User userEntity = userService.getUserByUsername(user.getUsername());
        if (userEntity == null) {
            return null;
        }
        return this.postRepository
                .findAllByDraftIsTrue()
                .stream()
                .filter(post -> {
                    User author = post.getAuthor();
                    if (author.getId() == user.getId()) {
                        return true;
                    }
                    return false;
                })
                .sorted((p1, p2) -> p2.getPostedOn().compareTo(p1.getPostedOn()))
                .map(post -> modelMapper.map(post, PostServiceModel.class))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<PostServiceModel> getPostById(Long id) {
        Optional<Post> postById = this.postRepository.findById(id);
        if (postById.isEmpty()) {
            return Optional.empty();
        }
        PostServiceModel postServiceModel = modelMapper
                .map(postById.get(), PostServiceModel.class);

        return Optional.of(postServiceModel);
    }

    @Override
    public void likePost(Long postId, String username) {
        Optional<Post> post = postRepository.findById(postId);
        User user = userService.getUserByUsername(username);
        if (post.isEmpty()) {
            return;
        }

        if (post.get().getLikers() != null) {
            post.get().getLikers().add(user);
            return;
        }
        post.get().setLikers(new HashSet<>());
        post.get().getLikers().add(user);
    }

    @Override
    public void dislikePost(Long postId, String username) {
        Optional<Post> post = postRepository.findById(postId);
        User user = userService.getUserByUsername(username);
        if (post.isEmpty()) {
            return;
        }

        if (post.get().getLikers() != null) {
            post.get().getLikers().remove(user);
            return;
        }
        post.get().setLikers(new HashSet<>());
    }

    @Override
    public void saveDraftAsPost(PostServiceModel draft) {
        Post post = modelMapper.map(draft, Post.class);
        post.setDraft(false);
        this.postRepository.saveAndFlush(post);
    }

    @Override
    public void deleteDraft(PostServiceModel draft) {
        Post post = modelMapper.map(draft, Post.class);
        this.postRepository.delete(post);
    }

    @Override
    public void deletePost(PostServiceModel postServiceModel) {
        Post post = modelMapper.map(postServiceModel, Post.class);
        this.postRepository.delete(post);
    }
}
