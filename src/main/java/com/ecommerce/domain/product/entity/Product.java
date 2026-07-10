package com.ecommerce.domain.product.entity;

import com.ecommerce.domain.product.dto.ProductCreate;
import com.ecommerce.domain.product.enums.ProductStatus;
import com.ecommerce.domain.product.vo.ModelNumber;
import com.ecommerce.domain.product.vo.OrderQuantityPolicy;
import com.ecommerce.domain.product.vo.Pricing;
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
import java.util.Optional;

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
    List<ProductCategory> productCategories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductOptionGroup> productOptionGroups = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProductSku> productSkus = new ArrayList<>();


    public static Product create(ProductCreate productCreate) {
        // Product 생성시 필수 필드
        Product product = new Product();
        product.status = ProductStatus.DRAFT;
        product.name = productCreate.name();
        product.pricing = productCreate.pricing();
        product.description = productCreate.description();
        product.modelNumber = productCreate.modelNumber();
        product.sellerId = productCreate.sellerId();
        product.saleUnit = productCreate.saleUnit();
        product.orderQuantityPolicy = productCreate.orderQuantityPolicy();
        assignProductCategories(productCreate, product);

        // Product 생성시 Optional 필드
        Optional.ofNullable(productCreate.optionGroupCreate())
                .ifPresent(optionGroupCreates -> {
                    optionGroupCreates.forEach(optionGroupCreate -> {
                        product.productOptionGroups.add(ProductOptionGroup.create(optionGroupCreate, product));
                    });
                });


        return product;
    }

    private static void assignProductCategories(ProductCreate productCreate, Product product) {
        List<ProductCategory> categories = new ArrayList<>();
        ProductCreate.ProductCategoriesCreate c = productCreate.productCategoryCreate();
        assignCategory(categories, c.primaryCategoryId(), true, 0, product);
        assignSecondaryCategories(product, c, categories);
        product.productCategories = categories;
    }

    private static void assignCategory(List<ProductCategory> categories, Long c, boolean isPrimary, int value, Product product) {
        categories.add(ProductCategory.create(
                new ProductCreate.ProductCategoryCreate(c, isPrimary, new SortOrder(value))
                , product)
        );
    }

    private static void assignSecondaryCategories(Product product, ProductCreate.ProductCategoriesCreate c, List<ProductCategory> categories) {
        int order = 1;
        for (Long id : c.secondaryCategoryIds()) {
            assignCategory(categories, id, false, order, product);
            order += 1;
        }
    }
}
