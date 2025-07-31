package com.bynry.stockflow.repository;
import com.bynry.stockflow.entity.Inventory;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("""
        SELECT i FROM Inventory i
        JOIN FETCH i.product p
        JOIN FETCH i.warehouse w
        JOIN FETCH w.company c
        WHERE c.id = :companyId
          AND i.quantity < i.threshold
    """)
    List<Inventory> findLowStockByCompanyId(@Param("companyId") Long companyId);
}

