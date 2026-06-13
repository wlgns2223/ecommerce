package com.ecommerce.application.user;

import com.ecommerce.application.provided.UserRegister;
import com.ecommerce.application.required.UserRepository;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.exception.DuplicatedEmailException;
import com.ecommerce.domain.user.required.PasswordEncoder;
import com.ecommerce.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserModifyingService implements UserRegister {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public User register(UserRegisterRequest registerRequest) {

        checkDuplicateEmail(registerRequest);
        User user = User.create(registerRequest, passwordEncoder);
        return userRepository.save(user);
    }

    private void checkDuplicateEmail(UserRegisterRequest registerRequest) {
        if (userRepository.existsByEmail(new Email(registerRequest.email()))) {
            throw new DuplicatedEmailException("이메일: " + registerRequest.email());
        }
    }
}
