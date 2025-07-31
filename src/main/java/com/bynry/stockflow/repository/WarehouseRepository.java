package com.bynry.stockflow.repository;
import com.bynry.stockflow.entity.Warehouse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByCompanyId(Long companyId);
}
