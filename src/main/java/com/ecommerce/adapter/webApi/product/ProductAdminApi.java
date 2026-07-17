package com.ecommerce.adapter.webApi.product;

import com.ecommerce.adapter.webApi.common.ApiResponse;
import com.ecommerce.adapter.webApi.product.dto.request.ProductCreateRequest;
import com.ecommerce.adapter.webApi.product.dto.response.ProductResponse;
import com.ecommerce.adapter.webApi.product.mapper.ProductMapper;
import com.ecommerce.application.product.provided.ProductModify;
import com.ecommerce.domain.product.entity.Product;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/admin/product")
@RequiredArgsConstructor
public class ProductAdminApi {

    private final ProductModify productModify;
    private final ProductMapper productMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductCreateRequest productCreateRequest,
                                                               @AuthenticationPrincipal Long id) {
        Product product = productModify.create(productCreateRequest.toProductCreate(id));
        ProductResponse response = productMapper.toResponse(product);
        URI location = URI.create("/api/product/" + response.id());
        return ResponseEntity.created(location).body(ApiResponse.ok(response));


    }

}
