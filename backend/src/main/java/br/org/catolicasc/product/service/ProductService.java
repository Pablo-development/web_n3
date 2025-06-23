package br.org.catolicasc.product.service;

import br.org.catolicasc.category.model.Category;
import br.org.catolicasc.category.service.CategoryService;
import br.org.catolicasc.product.exception.NoStockException;
import br.org.catolicasc.product.DTO.ProductDTO;
import br.org.catolicasc.product.model.Product;
import br.org.catolicasc.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, @Lazy CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Product registerProduct(ProductDTO productDTO) {
        Category category = categoryService.getCategoryById(productDTO.getCategory().getId());

        Product product = new Product(productDTO.getPrice(), productDTO.getQuantity(), category);
        return productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        findProductById(id);
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product productToUpdate = findProductById(id);
        productToUpdate.setQuantity(productDTO.getQuantity());
        productToUpdate.setPrice(productDTO.getPrice());
        return productRepository.save(productToUpdate);
    }

    public void decreaseStock(Long id, int quantity) {
        Product product = findProductById(id);
        if (product.hasStock(quantity)) {
            throw new NoStockException("Product does not have stock to decrease! ");
        }
        product.decreaseStock(quantity);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found! "));
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }
    public List<Product> getProductsByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> findByCategoryId(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }
    
}