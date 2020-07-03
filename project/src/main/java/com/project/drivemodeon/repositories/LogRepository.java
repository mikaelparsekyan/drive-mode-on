package com.project.drivemodeon.repositories;

import com.project.drivemodeon.model.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
