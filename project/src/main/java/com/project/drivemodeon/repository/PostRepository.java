package com.project.drivemodeon.repository;

import com.project.drivemodeon.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
