package com.ecommerce.application.user;

import com.ecommerce.application.user.provided.UserAdminRegister;
import com.ecommerce.application.user.required.UserRepository;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.enums.Role;
import com.ecommerce.domain.user.exception.DuplicatedEmailException;
import com.ecommerce.domain.user.required.PasswordEncoder;
import com.ecommerce.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminRegisterService implements UserAdminRegister {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerAdmin(UserRegisterRequest registerRequest) {
        checkDuplicateEmail(registerRequest);
        User user = User.create(registerRequest, Role.ADMIN, passwordEncoder);
        return userRepository.save(user);
    }

    private void checkDuplicateEmail(UserRegisterRequest registerRequest) {
        if (userRepository.existsByEmail(new Email(registerRequest.email()))) {
            throw new DuplicatedEmailException("이메일: " + registerRequest.email());
        }
    }
}
