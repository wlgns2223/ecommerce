package com.ecommerce.adapter.persistence.querydsl.support;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class QuerydslSortSupport {

    private static final Map<Class<?>, Map<String, ComparableExpressionBase<?>>> WHITELIST_CACHE = new ConcurrentHashMap<>();

    private QuerydslSortSupport() {
    }
    
    public static <E extends Enum<E> & SortField> OrderSpecifier<?>[] toOrderSpecifiers(Sort sort, Class<E> sortFiledType) {

        if (sort == null || sort.isUnsorted()) {
            return new OrderSpecifier[0];
        }

        Map<String, ComparableExpressionBase<?>> whitelist = whitelistOf(sortFiledType);

        return sort.stream()
                .map(order -> {
                    ComparableExpressionBase<?> path = whitelist.get(order.getProperty());
                    if (path == null) return null;
                    return order.isAscending() ? path.asc() : path.desc();
                })
                .filter(Objects::nonNull)
                .toArray(OrderSpecifier[]::new);

    }

    private static <E extends Enum<E> & SortField> Map<String, ComparableExpressionBase<?>> whitelistOf(
            Class<E> sortFieldType) {
        return WHITELIST_CACHE.computeIfAbsent(sortFieldType, ignored ->
                Arrays.stream(sortFieldType.getEnumConstants())
                        .collect(Collectors.toUnmodifiableMap(SortField::key, SortField::expression)));
    }


}
