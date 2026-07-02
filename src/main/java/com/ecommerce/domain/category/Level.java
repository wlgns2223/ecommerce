package com.ecommerce.domain.category;

import com.ecommerce.domain.category.exception.OutOfLevelException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Level(@Column(nullable = false, name = "level", columnDefinition = "INT")
                    Integer value) {
    private static final int LIMIT = 3;

    public Level {
        if (value > LIMIT) {
            throw new OutOfLevelException("카테고리 레벨을 초과하였습니다. 값: " + value);
        }
    }

    public static Level assignTopLevel() {
        return new Level(1);
    }

    public static Level addLevel(Level parentLevel) {
        return new Level(parentLevel.value() + 1);
    }
}
