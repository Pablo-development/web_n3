package br.org.catolicasc.product.model;

import br.org.catolicasc.category.model.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne()
    @JoinColumn(name = "category_id",  nullable = true)
    private Category category;

    public Product(Double price, int quantity, Category category) {
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public Product() {}


    public boolean hasStock(int quantity) {
        return this.quantity >= quantity;
    }

    public void increaseStock(int quantity) {
        this.quantity += quantity;
    }

    public void decreaseStock(int quantity) {
        this.quantity -= quantity;
    }
}