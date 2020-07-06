package com.project.drivemodeon.repository;

import com.project.drivemodeon.model.entity.Post;
import com.project.drivemodeon.model.entity.User;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByPostPrivacyLike(PostPrivacyEnum postPrivacyEnum);

    List<Post> findAll();
}
