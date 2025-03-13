package com.sideproject.caregiver_management.user.repository;

import com.sideproject.caregiver_management.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public User findOne(Long id){
        // EntityManager의 find는 기본키를 통해 조회한다.
        return em.find(User.class, id);
    }

    public void save(User user){
        em.persist(user);
    }

    public Optional<User> findByLoginId(String loginId){
        // SELECT * from user where user.login_id=*
        // 값이 없으면 예외가 난다. 따라서 예외 처리를 해줘야한다.
        // null을 직접 다루면 의도치 않은 문제가 생길 수 있으므로 안전하게 Optional을 사용해서 다른 클래스에 알려주자.
        try {
            // Optional.of -> null이 아닌 값을 Optional 객체로 만드는 함수 (null을 넣으면 에러가 발생한다)
            // Optional.ofNullable -> null이어도 안전하게 처리

            return Optional.of(em.createQuery("select u from User u where u.loginId = :loginId", User.class)
                    .setParameter("loginId", loginId)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
