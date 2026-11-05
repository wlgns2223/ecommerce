package com.ecommerce.application.product.provided.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;
import java.util.List;

public record ProductSkuCreateRequest(@NotNull Long productId,
                                      @Positive BigDecimal price,
                                      @PositiveOrZero int stock,
                                      @NotNull @UniqueElements
                                      List<@NotNull @Positive Long> optionValueIds
) {

}
