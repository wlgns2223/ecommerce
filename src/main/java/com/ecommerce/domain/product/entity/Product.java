package com.ecommerce.domain.product.entity;

import com.ecommerce.domain.product.enums.ProductStatus;
import com.ecommerce.domain.product.vo.ModelNumber;
import com.ecommerce.domain.product.vo.OrderQuantityPolicy;
import com.ecommerce.domain.product.vo.Pricing;
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
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Product extends BaseEntity {

    @Column(nullable = false)
    String name;

    @Embedded
    Pricing pricing;

    @Column(columnDefinition = "TEXT")
    String description;

    @Embedded
    ModelNumber modelNumber;

    @Column(nullable = false)
    Long sellerId;

    @Column
    String saleUnit;

    @Embedded
    OrderQuantityPolicy orderQuantityPolicy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ProductStatus status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductOptionGroup> productOptionGroups = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductSku> productSkus = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductCategory> productCategories = new ArrayList<>();
}
