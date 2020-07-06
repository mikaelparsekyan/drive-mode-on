package com.project.drivemodeon.repository;

import com.project.drivemodeon.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("UPDATE User u SET u.username = ?1 WHERE u.id = ?2")
    void updateUserById(String username, Long userId);
}