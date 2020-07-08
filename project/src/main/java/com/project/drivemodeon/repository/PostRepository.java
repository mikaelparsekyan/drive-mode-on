package com.project.drivemodeon.repository;

import com.project.drivemodeon.model.entity.Post;
import com.project.drivemodeon.validation.constant.enumeration.PostPrivacyEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.LinkedList;

public interface PostRepository extends JpaRepository<Post, Long> {
    LinkedList<Post> findAllByPostPrivacyLikeOrderByPostedOnDesc(PostPrivacyEnum postPrivacyEnum);

    @Query("SELECT p FROM Post p WHERE p.isDraft = false")
    LinkedList<Post> findAllByDraftIsFalse();
}
