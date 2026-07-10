package com.ecommerce.adapter.persistence.product;

import com.ecommerce.application.product.required.UserChecker;
import com.ecommerce.application.user.provided.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCheckerAdapter implements UserChecker {
    private final UserFinder userFinder;

    @Override
    public boolean existsById(Long id) {
        return userFinder.existsById(id);
    }
}
