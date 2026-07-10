package com.ecommerce.application.user;

import com.ecommerce.application.user.provided.UserFinder;
import com.ecommerce.application.user.required.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService implements UserFinder {

    private final UserRepository userRepository;

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
