package com.project.drivemodeon.repositories;

import com.project.drivemodeon.domain.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
