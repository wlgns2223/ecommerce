package com.ecommerce.application.user;

import com.ecommerce.application.user.provided.UserRegister;
import com.ecommerce.application.user.required.UserRepository;
import com.ecommerce.domain.user.dto.UserRegisterRequest;
import com.ecommerce.domain.user.entity.User;
import com.ecommerce.domain.user.exception.DuplicatedEmailException;
import com.ecommerce.domain.user.required.Encoder;
import com.ecommerce.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRegisterService implements UserRegister {

    private final UserRepository userRepository;
    private final Encoder encoder;


    @Override
    @Transactional
    public User register(UserRegisterRequest registerRequest) {

        checkDuplicateEmail(registerRequest);
        User user = User.create(registerRequest, encoder);
        return userRepository.save(user);
    }

    private void checkDuplicateEmail(UserRegisterRequest registerRequest) {
        if (userRepository.existsByEmail(new Email(registerRequest.email()))) {
            throw new DuplicatedEmailException("이메일: " + registerRequest.email());
        }
    }

}
