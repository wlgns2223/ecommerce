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
@Table(name = "product_categories")
@SQLDelete(sql = "UPDATE product_categories SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ProductCategory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    @Column(nullable = false)
    Long categoryId;

    @Column(nullable = false, columnDefinition = "BOOLEAN")
    Boolean isPrimary;

    @Embedded
    SortOrder sortOrder;

    public static ProductCategory create(ProductCreate.ProductCategoryCreate productCategoryCreate, Product product) {
        ProductCategory productCategoryEntity = new ProductCategory();
        productCategoryEntity.categoryId = productCategoryCreate.categoryId();
        productCategoryEntity.isPrimary = productCategoryCreate.isPrimary();
        productCategoryEntity.sortOrder = productCategoryCreate.sortOrder();
        productCategoryEntity.product = product;

        return productCategoryEntity;
    }

}
