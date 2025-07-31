package com.bynry.stockflow.service;
import com.bynry.stockflow.dto.SupplierDTO;
import com.bynry.stockflow.entity.Inventory;
import com.bynry.stockflow.entity.Product;
import com.bynry.stockflow.entity.Supplier;
import com.bynry.stockflow.repository.InventoryRepository;
import com.bynry.stockflow.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.bynry.stockflow.dto.LowStockAlertDTO;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AlertService {

    private final InventoryRepository inventoryRepository;
    private final SupplierRepository supplierRepository;

    public List<LowStockAlertDTO> getLowStockAlerts(Long companyId) {
        List<Inventory> lowStockInventories = inventoryRepository.findLowStockByCompanyId(companyId);

        List<LowStockAlertDTO> alerts = new ArrayList<>();

        for (Inventory inventory : lowStockInventories) {
            Product product = inventory.getProduct();

            List<Supplier> suppliers = supplierRepository.findByProductId(product.getId());
            Supplier supplier = suppliers.isEmpty() ? null : suppliers.get(0);

            SupplierDTO supplierDTO = supplier != null
                    ? new SupplierDTO(supplier.getId(), supplier.getName(), supplier.getContactEmail())
                    : null;

            LowStockAlertDTO alert = new LowStockAlertDTO(
                    product.getId(),
                    product.getName(),
                    product.getSku(),
                    inventory.getWarehouse().getId(),
                    inventory.getWarehouse().getName(),
                    inventory.getQuantity(),
                    inventory.getThreshold(),
                    12, // Static assumption for now
                    supplierDTO
            );

            alerts.add(alert);
        }

        return alerts;
    }
}
