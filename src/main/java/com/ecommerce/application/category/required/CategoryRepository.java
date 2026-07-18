package com.ecommerce.application.category.required;

import com.ecommerce.domain.category.entity.Category;
import com.ecommerce.domain.category.vo.Slug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsBySlug(Slug slug);

    boolean existsAllByIdIn(Collection<Long> ids);

    boolean existsById(@NonNull Long id);

    @Query(value = """
                WITH RECURSIVE sub AS (
                    SELECT id FROM categories
                        WHERE id = :rootId AND deleted_at IS NULL
                    UNION ALL
                    SELECT c.id FROM categories c
                        JOIN sub ON c.parent_id = sub.id
                    WHERE c.deleted_at IS NULL
                )
                            SELECT id FROM sub
            """, nativeQuery = true)
    List<Long> findSelfAndDescendantIds(@Param("rootId") Long rootId);
}
