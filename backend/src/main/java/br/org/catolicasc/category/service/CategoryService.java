package br.org.catolicasc.category.service;

import br.org.catolicasc.category.DTO.CategoryResponseDTO;
import br.org.catolicasc.category.model.Category;
import br.org.catolicasc.category.DTO.CategoryDTO;
import br.org.catolicasc.product.model.Product;
import br.org.catolicasc.product.service.ProductService;
import br.org.catolicasc.repository.CategoryRepository;
import br.org.catolicasc.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;


    public CategoryService(CategoryRepository categoryRepository, ProductService productService, ProductRepository productRepository, ProductService productService1) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productService = productService1;
    }

    public CategoryResponseDTO createCategory(CategoryDTO data) {
        Category category = new Category(data.name());
        categoryRepository.save(category);
        return new CategoryResponseDTO(category.getId(), category.getName());
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findCategoryById(id).orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    public List<CategoryResponseDTO> findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(category -> new CategoryResponseDTO(category.getId(), category.getName())).toList();
    }

    public CategoryResponseDTO updateCategory(Long id, CategoryDTO data) {
        Category category = getCategoryById(id);
        category.setName(data.name());
        categoryRepository.save(category);
        return new CategoryResponseDTO(category.getId(), category.getName());
    }


    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);

        List<Product> produtos = productService.getProductsByCategory(category);
        for (Product p : produtos) {
            p.setCategory(null);
        }
        productRepository.saveAll(produtos);
        categoryRepository.delete(category);
    }
}
