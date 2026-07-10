package com.ecommerce.application.product.required;

import java.util.List;

public interface CategoryChecker {
    boolean existsAll(List<Long> ids);
}
