# StockFlow – Inventory Management System

This project is a Spring Boot-based backend system built as part of the Bynry Backend Engineering Internship Case Study. It simulates a multi-warehouse inventory management platform for small businesses. Features include low-stock alerting, product-supplier relationships, and product bundling.

## Project Structure

* src/main/java/com/bynry/stockflow/ → core codebase
* SOLUTION.md → detailed write-up covering design, assumptions, and reasoning
* /entity → JPA entities
* /repository → Spring Data interfaces
* /service → business logic
* /controller → REST endpoints
* /dto → data transfer models

## Features

* Add and manage products
* Track inventory per warehouse
* Generate low-stock alerts for companies
* Manage supplier relationships
* Support for product bundles
* Clean JSON-based REST API

## Technologies Used

* Java 17
* Spring Boot 3.2
* Spring Data JPA
* MySQL
* Maven
* Lombok

## Database Setup

1. Create a database named bynry\_inventory:

```sql
CREATE DATABASE bynry_inventory;
```

2. Configure application.properties with your DB credentials:

```
spring.datasource.url=jdbc:mysql://localhost:3306/bynry_inventory
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

## How to Run

In the project root directory, use:

```bash
mvn spring-boot:run
```

This will start the application on port 8080.

## API Documentation

### Low Stock Alert API

Endpoint:

```
GET /api/companies/{companyId}/alerts/low-stock
```

Sample Response:

```json
{
  "alerts": [
    {
      "productId": 1,
      "productName": "Router",
      "sku": "SKU-RT-001",
      "warehouseId": 1,
      "warehouseName": "Main WH",
      "currentStock": 5,
      "threshold": 10,
      "daysUntilStockout": 12,
      "supplier": null
    }
  ],
  "total_alerts": 1
}
```

### Notes

* Alerts are generated only when current stock falls below the defined threshold.
* Only basic supplier info is returned (first supplier if multiple exist).
* Assumes a static value for daysUntilStockout (12) as no sales data is modeled.

## Author

Sahil Vankudre
