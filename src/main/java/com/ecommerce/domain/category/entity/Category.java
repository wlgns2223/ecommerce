package com.ecommerce.domain.category.entity;

import com.ecommerce.domain.category.Level;
import com.ecommerce.domain.category.dto.request.CategoryCreate;
import com.ecommerce.domain.category.vo.Slug;
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

import java.util.Optional;

@Getter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "categories")
@SQLDelete(sql = "UPDATE categories SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Category extends BaseEntity {

    @Column(nullable = false)
    String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // root는 null
    Category parent;

    @Embedded
    Slug slug;

    @Column(nullable = false)
    Boolean isActive;

    @Embedded
    Level level;

    public static Category create(CategoryCreate request) {

        Category category = new Category();
        category.name = request.name();
        category.slug = new Slug(request.slug());
        category.parent = request.parent();
        category.level = decideLevelFrom(request.parent());
        category.isActive = true;
        return category;
    }

    private static Level decideLevelFrom(Category parent) {
        return Optional.ofNullable(parent)
                .map(Category::getLevel)
                .map(Level::addLevel)
                .orElseGet(Level::assignTopLevel);
    }
}
