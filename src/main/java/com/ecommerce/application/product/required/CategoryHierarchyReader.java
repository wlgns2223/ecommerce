package com.ecommerce.application.product.required;

import java.util.List;

public interface CategoryHierarchyReader {

    List<Long> findSelfAndDescendantIds(Long rootId);
}
