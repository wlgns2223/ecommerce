package com.ecommerce.application.category.required;

import com.ecommerce.domain.category.entity.Category;
import com.ecommerce.domain.category.vo.Slug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsBySlug(Slug slug);
    
    boolean existsAllByIdIn(Collection<Long> ids);
}
