package com.ecommerce.domain.product.entity;

import com.ecommerce.domain.product.vo.Money;
import com.ecommerce.domain.product.vo.SkuCode;
import com.ecommerce.domain.product.vo.Stock;
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
@Table(name = "product_skus")
@SQLDelete(sql = "UPDATE product_skus SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ProductSku extends BaseEntity {

    @Embedded
    SkuCode skuCode;

    @Embedded
    @AttributeOverride(name = "amount",
            column = @Column(name = "price", precision = 10, scale = 2))
    Money price;

    @Embedded
    Stock stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

}
