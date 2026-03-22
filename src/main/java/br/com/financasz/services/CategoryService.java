package br.com.financasz.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.financasz.dtos.CategoryDTO;
import br.com.financasz.enums.CategoryTypeEnum;
import br.com.financasz.exceptions.AcessDeniedException;
import br.com.financasz.exceptions.CategoryAlreadyExistsException;
import br.com.financasz.exceptions.CategoryNotFoundException;
import br.com.financasz.exceptions.CategoryTypeDoesNotExistException;
import br.com.financasz.filters.CategoryFilter;
import br.com.financasz.models.Category;
import br.com.financasz.models.User;
import br.com.financasz.repositories.CategoryRepository;
import jakarta.transaction.Transactional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public CategoryDTO createCategory(CategoryDTO categoriaDTO) {
        User user = getAuthenticatedUser();

        if (categoriaDTO.getName() == null || categoriaDTO.getName().isEmpty()) {
            throw new RuntimeException("Nome da categoria é obrigatório");
        }

        CategoryFilter filter = new CategoryFilter();
        filter.setName(categoriaDTO.getName());
        filter.setUserId(user.getId()); // filter pra validações

        if (categoryRepository.findByNameAndUserId(filter).isPresent()) {
            throw new CategoryAlreadyExistsException("Categoria já existe com esse nome para esse usuário");
        }

        if (categoriaDTO.getActive() == null) {
            categoriaDTO.setActive(true);
        }

        Category category = modelMapper.map(categoriaDTO, Category.class);
        category.setUser(user);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    public List<CategoryDTO> getAllCategories(CategoryFilter filter) {

        filter.setUserId(getAuthenticatedUser().getId());

        List<Category> categories = categoryRepository.getAll(filter);

        List<CategoryDTO> categoriesResponse = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
        return categoriesResponse;
    }

    public Page<CategoryDTO> getAllCategoriesPaged(CategoryFilter filter, Pageable pageable) {

        filter.setUserId(getAuthenticatedUser().getId());

        Page<Category> categoriesPage = categoryRepository.getAllPaged(filter, pageable);

        return categoriesPage.map(category -> modelMapper.map(category, CategoryDTO.class));
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException());
        return modelMapper.map(category, CategoryDTO.class);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        var usuarioAuthID = getAuthenticatedUser().getId();

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException());

        if (!category.getUser().getId().equals(usuarioAuthID)) {
            throw new AcessDeniedException("Você não tem permissão para atualizar esta categoria.");
        }

        if (categoryDTO.getName() != null && categoryDTO.getName().isBlank()) {
            throw new RuntimeException("Nome não pode ser vazio");
        }

        if (categoryDTO.getActive() != null) {
            category.setActive(categoryDTO.getActive());
        }

        if (categoryDTO.getName() != null && !categoryDTO.getName().equals(category.getName())) {

            CategoryFilter filter = new CategoryFilter();
            filter.setName(categoryDTO.getName());
            filter.setUserId(usuarioAuthID);

            boolean exists = categoryRepository
                    .findByNameAndUserId(filter).isPresent();

            if (exists) {
                throw new CategoryAlreadyExistsException();
            } else {
                category.setName(categoryDTO.getName());
            }
        }

        if (categoryDTO.getType() != null) {
            try {
                category.setType(CategoryTypeEnum.valueOf(categoryDTO.getType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new CategoryTypeDoesNotExistException();
            }
        }

        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
