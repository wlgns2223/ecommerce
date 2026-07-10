package com.ecommerce.domain.product.entity;

import com.ecommerce.domain.product.dto.ProductCreate;
import com.ecommerce.domain.product.vo.SortOrder;
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
@Table(name = "product_option_values")
@SQLDelete(sql = "UPDATE product_option_values SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ProductOptionValue extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id", nullable = false)
    ProductOptionGroup productOptionGroup;

    @Column(nullable = false)
    String value;

    @Embedded
    SortOrder sortOrder;

    public static ProductOptionValue create(ProductCreate.OptionValueCreate optionValueCreate) {
        ProductOptionValue productOptionValue = new ProductOptionValue();

        productOptionValue.value = optionValueCreate.value();
        productOptionValue.sortOrder = optionValueCreate.sortOrder();
        productOptionValue.productOptionGroup = optionValueCreate.group();

        return productOptionValue;

    }

}
