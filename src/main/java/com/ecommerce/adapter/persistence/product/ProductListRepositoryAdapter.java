package com.ecommerce.adapter.persistence.product;

import com.ecommerce.adapter.persistence.querydsl.support.QuerydslSortSupport;
import com.ecommerce.adapter.webApi.product.dto.response.ProductSummary;
import com.ecommerce.adapter.webApi.product.dto.response.QProductSummary;
import com.ecommerce.application.product.provided.dto.ProductSearchCondition;
import com.ecommerce.application.product.required.CategoryHierarchyReader;
import com.ecommerce.application.product.required.ProductListRepository;
import com.ecommerce.domain.product.entity.QProduct;
import com.ecommerce.domain.product.enums.ProductStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ecommerce.domain.product.entity.QProductCategory.productCategory;

@Repository
@RequiredArgsConstructor
public class ProductListRepositoryAdapter implements ProductListRepository {

    private final JPAQueryFactory queryFactory;
    private final CategoryHierarchyReader categoryHierarchyReader;


    @Override
    public Page<ProductSummary> searchProducts(ProductSearchCondition searchCondition, Pageable pageable) {

        QProduct product = QProduct.product;

        BooleanExpression[] whereFilters = new BooleanExpression[]{
                saleOnly(product),
                categoryIdIn(product, resolveCategoryIds(searchCondition.categoryId())),
                keywordContains(product, searchCondition.keyword()),
                salePriceGoe(product, searchCondition.minPrice()),
                salePriceLoe(product, searchCondition.maxPrice()),
                sellerIdEq(product, searchCondition.sellerId())
        };

        List<ProductSummary> searchResults = queryFactory.select(
                        new QProductSummary(
                                product.id,
                                product.createdAt,
                                product.updatedAt,
                                product.name,
                                product.description,
                                product.modelNumber.value,
                                product.pricing.salePrice.amount,
                                product.pricing.retailPrice.amount
                        )
                ).from(product)
                .where(whereFilters)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderBy(product, pageable.getSort()))
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(product.count())
                .from(product)
                .where(whereFilters);

        return PageableExecutionUtils.getPage(searchResults, pageable, countQuery::fetchOne);
    }

    private List<Long> resolveCategoryIds(Long rootId) {
        if (rootId == null) return List.of();

        return categoryHierarchyReader.findSelfAndDescendantIds(rootId);
    }

    private BooleanExpression categoryIdIn(QProduct product, List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) return null;
        return JPAExpressions
                .selectOne()
                .from(productCategory)
                .where(productCategory.product.eq(product)
                        .and(productCategory.categoryId.in(categoryIds)))
                .exists();
    }

    private BooleanExpression keywordContains(QProduct product, String keyword) {
        return (keyword != null && !keyword.isBlank()) ? product.name.contains(keyword) : null;
    }

    private BooleanExpression salePriceGoe(QProduct product, BigDecimal minPrice) {
        return minPrice != null ? product.pricing.salePrice.amount.goe(minPrice) : null;
    }

    private BooleanExpression salePriceLoe(QProduct product, BigDecimal maxPrice) {
        return maxPrice != null ? product.pricing.salePrice.amount.loe(maxPrice) : null;
    }

    private BooleanExpression sellerIdEq(QProduct product, Long sellerId) {
        return sellerId != null ? product.sellerId.eq(sellerId) : null;
    }

    private BooleanExpression saleOnly(QProduct product) {
        return product.status.eq(ProductStatus.ON_SALE);
    }


    private OrderSpecifier<?>[] orderBy(QProduct product, Sort sort) {
        ArrayList<OrderSpecifier<?>> orderSpecifiers =
                new ArrayList<>(List.of(QuerydslSortSupport.toOrderSpecifiers(sort, ProductSortField.class)));

        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(product.createdAt.desc());
        }
        orderSpecifiers.add(product.id.desc());

        // OrderSpecifier[]::new는 런타임에 OrderSpecifier로 넘기기 위한 관용구
        return orderSpecifiers.toArray(OrderSpecifier[]::new);

    }

}
