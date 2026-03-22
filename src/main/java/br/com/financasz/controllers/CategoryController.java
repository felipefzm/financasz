package br.com.financasz.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.financasz.config.RestControllerPath;
import br.com.financasz.dtos.CategoryDTO;
import br.com.financasz.filters.CategoryFilter;
import br.com.financasz.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(RestControllerPath.CATEGORIES)
@Tag(name = "Categories", description = "Endpoints relacionados a categorias/Endpoints related to categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Listar todas as categorias possibilitando uso de filtros / List all categories with the possibility of using different filters", description = "Retorna uma lista com todas as categorias com base no filtro, não enviando nenhum, retorna todas / It returns a list of all the categories in the system, based on the filters, if you don't send any in the parameters, it returns all of them")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso / Categories list successfully returned"),
    })
    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAllCategories(CategoryFilter filter) {
        List<CategoryDTO> categories = categoryService.getAllCategories(filter);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Listar todas as categorias possibilitando uso de filtros, usando paginação / List all categories with the possibility of using different filters, with pagination", description = "Retorna uma lista com todas as categorias com base no filtro, com limite baseado na paginação definida. Não enviando nenhum, retorna todas / It returns a list of all the categories in the system, based on the filters, with the limit based on the pagination parameters. If you don't send any parameter in the filters, it returns all of them")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso / Categories list successfully returned"),
    })
    @GetMapping(RestControllerPath.PAGED)
    public ResponseEntity<Page<CategoryDTO>> getAllCategoriesPaged(CategoryFilter filter, Pageable pageable) {
        Page<CategoryDTO> categories = categoryService.getAllCategoriesPaged(filter, pageable);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Retorna uma categoria por id / List a category by its ID", description = "Retorna uma categoria com base no seu id enviado / It returns a category based on the sent ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria retornada com sucesso / Category successfully returned"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada / Category not found")
    })
    @GetMapping(RestControllerPath.ID)
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Criar nova categoria / Create a new category", description = "Cria uma nova categoria não existente / It creates a new category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso / Category successfully created"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos / Invalid data")
    })
    @PostMapping()
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(201).body(createdCategory);
    }

    @Operation(summary = "Atualizar categoria por ID / Update category by ID", description = "Atualiza uma categoria existente / It updates an existing category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso / Category successfully updated"),
        @ApiResponse(responseCode = "404", description = "Categoria não localizada / Category not found")
    })
    @PutMapping(RestControllerPath.ID)
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    @Operation(summary = "Excluir categoria por ID / Delete category by ID", description = "Remove uma categoria existente / It deletes an existing category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso / Category successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Categoria não localizada / Category not found")
    })
    @DeleteMapping(RestControllerPath.ID)
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
