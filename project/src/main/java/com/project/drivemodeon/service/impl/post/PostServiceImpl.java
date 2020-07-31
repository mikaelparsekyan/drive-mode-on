package com.project.drivemodeon.service.impl.post;

import com.project.drivemodeon.model.entity.Post;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.model.service.post.PostServiceModel;
import com.project.drivemodeon.model.service.user.UserServiceModel;
import com.project.drivemodeon.model.view.PostViewModel;
import com.project.drivemodeon.repository.PostRepository;
import com.project.drivemodeon.service.api.post.PostService;
import com.project.drivemodeon.service.api.user.UserService;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
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

        if (postServiceModel.getImageFile() != null) {
            uploadImageToPost(postServiceModel.getImageFile(), post);
        }
    }

    private void uploadImageToPost(MultipartFile image, Post post) {
        List<String> allowedExtensions = new ArrayList<>(Arrays.asList("jpg", "jpeg", "png"));
        String fileExtension = image.getOriginalFilename()
                .substring(image.getOriginalFilename().lastIndexOf(".") + 1);

        if (allowedExtensions.contains(fileExtension)) {
            String path = "/upload/user/" + post.getAuthor().getUsername().toLowerCase() + "/posts";
            File pathToImage = new File("src/main/resources/static" + path);
            pathToImage.mkdirs();

            if (pathToImage.exists()) {
                try {
                    String imageFileString = "post_" + post.getId() + "." + fileExtension;
                    File file = new File("src/main/resources/static" + path, imageFileString);

                    org.apache.commons.io.FileUtils.writeByteArrayToFile(file, image.getBytes());

                    post.setImagePath(path + "/" + imageFileString);
                    this.postRepository.saveAndFlush(post);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public LinkedList<PostViewModel> getAllPostsByPrivacy(PostPrivacyEnum postPrivacyEnum) {
        return this.postRepository
                .findAllByPostPrivacyLikeOrderByPostedOnDesc(postPrivacyEnum)
                .stream()
                .map(post -> modelMapper.map(post, PostViewModel.class))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public LinkedList<PostViewModel> getAllFeedPostsByUser(UserServiceModel user) {
        User userEntity = userService.getUserByUsername(user.getUsername());
        if (userEntity == null) {
            return null;
        }
        return this.postRepository
                .findAllByDraftIsFalse()
                .stream()
                .filter(post -> {
                    User postAuthor = post.getAuthor();
                    if (userEntity.getFollowing().contains(postAuthor) ||
                            postAuthor.getId() == user.getId()) {
                        if(post.getPostPrivacy() == PostPrivacyEnum.ONLY_ME && postAuthor.getId() == userEntity.getId()){
                            return true;
                        }
                        return post.getPostPrivacy() != PostPrivacyEnum.ONLY_ME;
                    }
                    return false;
                })
                .map(post -> modelMapper.map(post, PostViewModel.class))
                .sorted((p1, p2) -> p2.getPostedOn().compareTo(p1.getPostedOn()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public LinkedList<PostViewModel> getAllDraftsByUser(UserServiceModel user) {
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
                .map(post -> modelMapper.map(post, PostViewModel.class))
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
        Optional<Post> draftById = this.postRepository.findById(draft.getId());
        if (draftById.isPresent()) {
            draftById.get().setDraft(false);
            this.postRepository.saveAndFlush(draftById.get());
        }
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
