package com.ecommerce.application.product.provided.dto.request;

import java.math.BigDecimal;
import java.util.List;

public record ProductSkuAssignment(Long productId,
                                   BigDecimal price,
                                   int stock,
                                   List<Long> optionValueIds
) {

}
