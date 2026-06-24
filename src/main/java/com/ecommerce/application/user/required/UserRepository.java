package com.ecommerce.application.user.required;

import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(Email email);

    Optional<User> findByEmail(Email email);
}
