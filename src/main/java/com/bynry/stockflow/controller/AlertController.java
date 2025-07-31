package com.bynry.stockflow.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import com.bynry.stockflow.dto.LowStockAlertDTO;
import com.bynry.stockflow.service.AlertService;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/{companyId}/alerts/low-stock")
    public ResponseEntity<Map<String, Object>> getLowStockAlerts(@PathVariable Long companyId) {
        List<LowStockAlertDTO> alerts = alertService.getLowStockAlerts(companyId);

        Map<String, Object> response = new HashMap<>();
        response.put("alerts", alerts);
        response.put("total_alerts", alerts.size());

        return ResponseEntity.ok(response);
    }
}

