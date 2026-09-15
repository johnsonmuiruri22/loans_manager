package com.loan_manager_app.loans_manager.AUDIT.modal;


import com.loan_manager_app.loans_manager.SHARED.ENUMS.LogType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private LogType logType;
    private String activity;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String operatingSystem;
    private String deviceName;
    private String deviceType;
    private String userAgent;
}
