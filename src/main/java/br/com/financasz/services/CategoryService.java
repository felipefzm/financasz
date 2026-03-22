package br.com.financasz.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.financasz.dtos.CategoryDTO;
import br.com.financasz.enums.CategoryTypeEnum;
import br.com.financasz.exceptions.CategoryNotFoundException;
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

        if(categoriaDTO.getName() == null || categoriaDTO.getName().isEmpty()) {
            throw new RuntimeException("Nome da categoria é obrigatório");
        }

        if(categoryRepository.findByNameAndUserId(categoriaDTO.getName(), user.getId()).isPresent()) {
            throw new RuntimeException("Você já possui uma categoria com esse nome");
        }

        if (categoriaDTO.getActive() == null) {
            categoriaDTO.setActive(true);
        }

        if(categoriaDTO.getType() == null || (!categoriaDTO.getType().equalsIgnoreCase("REVENUE") && !categoriaDTO.getType().equalsIgnoreCase("EXPENSE"))) {
            throw new RuntimeException("Tipo da categoria inválido");
        }

        Category category = modelMapper.map(categoriaDTO, Category.class);
        category.setUser(user);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoriesResponse = categories.stream()
        .map(category -> modelMapper.map(category, CategoryDTO.class))
        .collect(Collectors.toList());
        return categoriesResponse;
    }

    public List<CategoryDTO> getAllCategoriesPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getCategoriesByType(String type) {
        List<Category> categories = categoryRepository.findByTypeAndUserId(CategoryTypeEnum.valueOf(type.toUpperCase()), getAuthenticatedUser().getId());
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException());
        return modelMapper.map(category, CategoryDTO.class);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException());
        modelMapper.map(categoryDTO, category);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

}
