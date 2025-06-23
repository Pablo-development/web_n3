package br.org.catolicasc.category.controller;

import br.org.catolicasc.category.DTO.CategoryDTO;
import br.org.catolicasc.category.DTO.CategoryResponseDTO;

import br.org.catolicasc.category.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }


    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAll() {
        return ResponseEntity.ok().body(categoryService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryResponseDTO category = categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryResponseDTO category = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CategoryResponseDTO> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
