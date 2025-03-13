package com.sideproject.caregiver_management.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 무분별한 new User()을 막자
// @Builder 을 쓰면 @AllArgsConstructor 을 써야하는데 필드 순서에 의존하므로 위험하다.
// 따라서 수동으로 필요한 항목만 직접 지정해서 순서가 의도치 않게 바뀌는 문제를 해결해보자.
public class User {
    @Id
    @GeneratedValue
    @Getter
    @Column(name="user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    @Column(name="login_id", nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Builder
    public User(Tenant tenant, String loginId, String name, String password) {
        this.tenant = tenant;
        this.loginId = loginId;
        this.name = name;
        this.password = password;
    }
}
