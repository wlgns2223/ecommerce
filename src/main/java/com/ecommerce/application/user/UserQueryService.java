package com.ecommerce.application.user;

import com.ecommerce.application.user.provided.UserFinder;
import com.ecommerce.application.user.required.UserRepository;
import com.ecommerce.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryService implements UserFinder {

    private final UserRepository userRepository;

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
}
