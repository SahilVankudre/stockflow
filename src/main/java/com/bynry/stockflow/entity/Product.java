package com.bynry.stockflow.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = "sku"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String sku;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ProductType productType; // SINGLE, BUNDLE

    @ManyToMany(mappedBy = "products")
    private List<Supplier> suppliers;
}
