package com.project.drivemodeon.repository;

import com.project.drivemodeon.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
