package com.ecommerce.application.required;

import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(Email email);
}
