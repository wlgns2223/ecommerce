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

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "product_option_groups")
@SQLDelete(sql = "UPDATE product_option_groups SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ProductOptionGroup extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @OneToMany(mappedBy = "productOptionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductOptionValue> productOptionValues = new ArrayList<>();

    @Column(nullable = false)
    String name;

    @Embedded
    SortOrder sortOrder;

    public static ProductOptionGroup create(ProductCreate.OptionGroupCreate optionGroupCreate, Product product) {
        ProductOptionGroup productOptionGroup = new ProductOptionGroup();

        productOptionGroup.name = optionGroupCreate.name();
        productOptionGroup.sortOrder = optionGroupCreate.sortOrder();
        productOptionGroup.product = product;
        optionGroupCreate.optionValueCreates()
                .forEach((dto) -> productOptionGroup
                        .productOptionValues
                        .add(ProductOptionValue.create(dto))
                );


        return productOptionGroup;
    }

}
