package com.ecommerce.application.user.provided;

import com.ecommerce.domain.user.dto.UserRegister;
import com.ecommerce.domain.user.entity.User;

public interface UserAdminRegister {
    User registerAdmin(UserRegister registerRequest);
}
