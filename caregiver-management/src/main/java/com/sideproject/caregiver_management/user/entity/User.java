package com.sideproject.caregiver_management.user.entity;

import com.sideproject.caregiver_management.auth.converter.EncodedPasswordConverter;
import com.sideproject.caregiver_management.auth.dto.EncodedPassword;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "users")  // 테스트시 사용하는 H2 데이터베이스에서 "user"는 예약어이므로 다른 이름을 사용해야함.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 무분별한 new User()을 막자
// @Builder 을 쓰면 @AllArgsConstructor 을 써야하는데 필드 순서에 의존하므로 위험하다.
// 따라서 수동으로 필요한 항목만 직접 지정해서 순서가 의도치 않게 바뀌는 문제를 해결해보자.
public class User {
    @Id
    @GeneratedValue
    @Column(name="user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(name="login_id", nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Convert(converter = EncodedPasswordConverter.class)
    private EncodedPassword password;

    @Builder
    public User(Tenant tenant, String loginId, String name, EncodedPassword password) {
        this.tenant = tenant;
        this.loginId = loginId;
        this.name = name;
        this.password = password;
    }
}
