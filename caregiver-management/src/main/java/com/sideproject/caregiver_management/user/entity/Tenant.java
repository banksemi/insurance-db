package com.sideproject.caregiver_management.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Tenant {
    @Id
    @GeneratedValue
    @Getter
    @Column(name = "tenant_id")
    private Long id; // Long을 사용하는 이유: null을 사용할 수 있어 id=0과 구분할 수 있다. (https://www.inflearn.com/community/questions/260916/id%EA%B0%92%EC%9D%84-long-%ED%83%80%EC%9E%85%EC%9C%BC%EB%A1%9C-%EC%A7%80%EC%A0%95%ED%95%98%EB%8A%94-%EC%9D%B4%EC%9C%A0%EA%B0%80-%EB%94%B0%EB%A1%9C-%EC%9E%88%EB%82%98%EC%9A%94)

    @Getter @Setter
    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "footer_message")
    private String FooterMessage;
}