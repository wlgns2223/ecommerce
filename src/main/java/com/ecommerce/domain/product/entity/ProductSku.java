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

import java.util.ArrayList;
import java.util.List;

/**
 * SKU(Stock Keeping Unit) — 상품의 실제 판매·재고 관리 단위.
 *
 * <p><b>탄생 배경</b><br>
 * 하나의 {@link Product}(예: "면 티셔츠")는 색상·사이즈 같은 여러 옵션을 가질 수 있고,
 * 가격과 재고는 상품 전체가 아니라 <b>옵션 값의 조합</b>마다 달라진다.
 * 이 "판매 가능한 옵션 조합 하나"를 독립적으로 관리하려고 분리한 엔티티가 ProductSku다.
 * 판매자는 가능한 전체 조합이 아니라 <b>실제로 파는 일부 조합만</b> SKU로 등록한다.
 *
 * <p><b>어떤 조합인지 표현하는 법</b><br>
 * ProductSku 자체는 옵션 값을 직접 들고 있지 않다. 어떤 옵션 값들의 조합인지는
 * {@link SkuOptionValue}(SKU ↔ {@link ProductOptionValue} 조인)로 연결한다.
 * 즉 "빨강 + M" SKU 하나는 SkuOptionValue 2행("빨강", "M")으로 표현된다.
 *
 * <p><b>보유 정보</b>
 * <ul>
 *   <li>{@code skuCode} : 조합을 식별하는 고유 코드(UUID 자동 생성)</li>
 *   <li>{@code price}   : 이 조합의 실판매가(상품 정가와 별개, 조합마다 다를 수 있음)</li>
 *   <li>{@code stock}   : 이 조합의 재고 수량</li>
 *   <li>{@code product} : 이 SKU가 속한 상품</li>
 * </ul>
 *
 * <p><b>예시</b> — 상품 "면 티셔츠"(옵션: 색상, 사이즈)
 * <pre>
 *   SKU #1  빨강 + M   price 18,000  stock 50
 *   SKU #2  파랑 + L   price 19,000  stock 30
 * </pre>
 * 고객이 "빨강 / M"을 주문하면 SKU #1이 선택되어 그 재고가 차감된다.
 */

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

    @OneToMany(mappedBy = "productSku", cascade = CascadeType.ALL, orphanRemoval = true)
    List<SkuOptionValue> skuOptionValues = new ArrayList<>();


}
