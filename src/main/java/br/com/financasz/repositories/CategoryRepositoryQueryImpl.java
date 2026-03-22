package br.com.financasz.repositories;

import java.util.List;
import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import br.com.financasz.filters.CategoryFilter;
import br.com.financasz.models.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CategoryRepositoryQueryImpl implements CategoryRepositoryQuery {

    @PersistenceContext
    private EntityManager em;
    
    
    @Override
    public Optional<Category> findByNameAndUserId(CategoryFilter filter) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QCategory category = QCategory.category;    
    }

    @Override
    public List<Category> findByTypeAndUserId(CategoryFilter filter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByTypeAndUserId'");
    }
    
}
