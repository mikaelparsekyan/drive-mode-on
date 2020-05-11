package com.project.drivemodeon.repositories;

import com.project.drivemodeon.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);

    @Query("SELECT u.followers FROM User u WHERE u.username = :username")
    Set<User> getAllFollowersByUsername(@Param("username") String username);

    @Query("SELECT u.following FROM User u WHERE u.username = :username")
    Set<User> getAllFollowingsByUsername(@Param("username") String username);
}