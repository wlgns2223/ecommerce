package com.ecommerce.domain.user.required;

public interface Encoder {

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);

}
