package com.project.drivemodeon.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users_logs")
@Data
public class Log extends BaseEntity {
    @Column(name = "user_ip_address")
    private String userIpAddress;

    @Column(name = "page_visit_date_time")
    private LocalDateTime pageVisitDateTime;

    @Column(name = "request_type")
    private String requestType;

    @Column(name = "page_url")
    private String pageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userId;
}
