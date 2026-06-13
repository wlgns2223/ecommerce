package com.ecommerce.domain.user.entity;

import com.ecommerce.domain.user.dto.UserDetailCreateRequest;
import com.ecommerce.domain.user.enums.TierCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailTest {

    @Test
    void createUserDetail() {
        UserDetailCreateRequest request = UserDetailFixture.createUserDetailRequest();
        UserDetail detail = UserDetail.createUserDetail(request);

        assertThat(detail.getTierCode()).isEqualTo(TierCode.BASIC);
    }
}