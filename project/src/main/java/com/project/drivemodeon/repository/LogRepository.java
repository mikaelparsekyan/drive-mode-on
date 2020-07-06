package com.project.drivemodeon.repository;

import com.project.drivemodeon.model.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
