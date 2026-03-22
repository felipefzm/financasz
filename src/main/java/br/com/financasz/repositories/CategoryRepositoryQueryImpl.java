package br.com.financasz.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import br.com.financasz.enums.CategoryTypeEnum;
import br.com.financasz.exceptions.CategoryTypeDoesNotExistException;
import br.com.financasz.filters.CategoryFilter;
import br.com.financasz.models.Category;
import br.com.financasz.models.QCategory;
import jakarta.persistence.EntityManager;

public class CategoryRepositoryQueryImpl implements CategoryRepositoryQuery {

    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryQueryImpl(EntityManager em) {
        if (em == null) {
            throw new IllegalArgumentException("EntityManager não pode ser nulo");
        }
        this.queryFactory = new JPAQueryFactory(em);
    }

    QCategory qCategory = QCategory.category;

    @Override
    public Optional<Category> findByNameAndUserId(CategoryFilter filter) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qCategory.user.id.eq(filter.getUserId()));
        builder.and(qCategory.name.eq(filter.getName()));

        Category result = queryFactory.selectFrom(qCategory)
                .where(builder)
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Category> findByTypeAndUserId(CategoryFilter filter) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qCategory.user.id.eq(filter.getUserId()));

        if (filter.getType() != null && !filter.getType().isBlank()) {
            try {
                CategoryTypeEnum typeEnum = CategoryTypeEnum.valueOf(filter.getType().toUpperCase());

                builder.and(qCategory.type.eq(typeEnum));

            } catch (IllegalArgumentException e) {
                throw new CategoryTypeDoesNotExistException();
            }
        }
        Category result = queryFactory.selectFrom(qCategory)
                .where(builder)
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<Category> getAll(CategoryFilter filter) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qCategory.user.id.eq(filter.getUserId()));

        if (filter.getName() != null && !filter.getName().isBlank()) {
            builder.and(qCategory.name.containsIgnoreCase(filter.getName()));
        }

        if (filter.getType() != null && !filter.getType().isBlank()) {
            try {
                CategoryTypeEnum typeEnum = CategoryTypeEnum.valueOf(filter.getType().toUpperCase());

                builder.and(qCategory.type.eq(typeEnum));

            } catch (IllegalArgumentException e) {
                throw new CategoryTypeDoesNotExistException();
            }
        }

        if (filter.getId() != null) {
            builder.and(qCategory.id.eq(filter.getId()));
        }

        if (filter.getActive() != null) {
            builder.and(qCategory.active.eq(filter.getActive()));
        }

        return queryFactory.selectFrom(qCategory)
                .where(builder)
                .fetch();
    }

    @Override
    public Page<Category> getAllPaged(CategoryFilter filter, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qCategory.user.id.eq(filter.getUserId()));

        if (filter.getName() != null && !filter.getName().isBlank()) {
            builder.and(qCategory.name.containsIgnoreCase(filter.getName()));
        }

        if (filter.getType() != null && !filter.getType().isBlank()) {
            try {
                CategoryTypeEnum typeEnum = CategoryTypeEnum.valueOf(filter.getType().toUpperCase());

                builder.and(qCategory.type.eq(typeEnum));

            } catch (IllegalArgumentException e) {
                throw new CategoryTypeDoesNotExistException();
            }
        }

        if (filter.getId() != null) {
            builder.and(qCategory.id.eq(filter.getId()));
        }

        if (filter.getActive() != null) {
            builder.and(qCategory.active.eq(filter.getActive()));
        }

        List<Category> categories = queryFactory.selectFrom(qCategory)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(qCategory.count())
                .from(qCategory)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(categories, pageable, total);

    }

}
