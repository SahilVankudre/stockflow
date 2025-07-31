package com.bynry.stockflow.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "warehouses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}

