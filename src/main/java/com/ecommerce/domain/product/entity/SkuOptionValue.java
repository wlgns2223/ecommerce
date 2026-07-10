package com.ecommerce.domain.product.entity;

import com.ecommerce.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "sku_option_values")
@SQLDelete(sql = "UPDATE sku_option_values SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class SkuOptionValue extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id", nullable = false)
    ProductSku productSku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_value_id", nullable = false)
    ProductOptionValue productOptionValue;

    public static SkuOptionValue create(ProductSku productSku, ProductOptionValue productOptionValue) {
        SkuOptionValue skuOptionValue = new SkuOptionValue();

        skuOptionValue.productSku = productSku;
        skuOptionValue.productOptionValue = productOptionValue;

        return skuOptionValue;
    }

}
