package com.ecommerce.domain.user.enums;

import java.util.List;

public enum Role {
    USER,
    ADMIN,
    ;

    public List<Role> expand() {
        return this == ADMIN ? List.of(USER, ADMIN) : List.of(this);
    }
}
