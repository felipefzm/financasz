package br.com.financasz.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.financasz.filters.CategoryFilter;
import br.com.financasz.models.Category;

@Repository
public interface CategoryRepositoryQuery {

    Optional<Category> findByNameAndUserId(CategoryFilter filter);

    List<Category> findByTypeAndUserId(CategoryFilter filter);

    List<Category> getAll(CategoryFilter filter);

    Page<Category> getAllPaged(CategoryFilter filter, Pageable pageable);

}
