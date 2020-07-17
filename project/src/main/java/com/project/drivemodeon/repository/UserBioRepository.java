package com.project.drivemodeon.repository;

import com.project.drivemodeon.model.entity.UserBio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBioRepository extends JpaRepository<UserBio, Long> {
}
