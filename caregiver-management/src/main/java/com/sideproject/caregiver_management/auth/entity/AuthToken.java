package com.sideproject.caregiver_management.auth.entity;

import com.sideproject.caregiver_management.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthToken {
    @Id
    @Column(name = "token_key", nullable = false) // KEY는 MYSQL(+MARIADB)에서 예약어
    @Size(min = 32, max = 1024)
    private String key;

    @Column(nullable = false)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiredAt;
}
