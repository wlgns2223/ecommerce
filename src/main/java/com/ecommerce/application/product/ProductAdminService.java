package com.ecommerce.application.product;

import com.ecommerce.application.product.provided.ProductModify;
import com.ecommerce.application.product.required.CategoryChecker;
import com.ecommerce.application.product.required.ProductAdminRepository;
import com.ecommerce.application.product.required.UserChecker;
import com.ecommerce.domain.product.dto.ProductCreate;
import com.ecommerce.domain.product.entity.Product;
import com.ecommerce.domain.product.exception.SellerNotFoundException;
import com.ecommerce.domain.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAdminService implements ProductModify {

    private final ProductAdminRepository productAdminRepository;
    private final CategoryChecker categoryChecker;
    private final UserChecker userChecker;

    @Override
    @Transactional
    public Product create(ProductCreate productCreate) {
        if (!userChecker.existsById(productCreate.sellerId())) {
            throw new SellerNotFoundException("등록되지 않은 판매자입니다.");
        }

        List<Long> ids = getCategoryIds(productCreate);
        if (!categoryChecker.existsAll(ids)) {
            throw new NotFoundException("카테고리를 찾을 수 없습니다.");
        }


        Product product = Product.create(productCreate);
        return productAdminRepository.save(product);
    }

    @NonNull
    private static List<Long> getCategoryIds(ProductCreate productCreate) {
        List<Long> ids = new ArrayList<>(productCreate.productCategoryCreate().secondaryCategoryIds());
        ids.add(productCreate.productCategoryCreate().primaryCategoryId());
        return ids;
    }

}
