package com.bynry.stockflow.repository;
import com.bynry.stockflow.entity.Supplier;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    @Query("""
        SELECT s FROM Supplier s
        JOIN s.products p
        WHERE p.id = :productId
    """)
    List<Supplier> findByProductId(@Param("productId") Long productId);
}
