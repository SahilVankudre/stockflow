# Bynry Backend Intern Case Study Submission

Name: Sahil Vankudre
Date: 2025-08-1
GitHub Repository: [https://github.com/your-username/stockflow](https://github.com/your-username/stockflow)

---

## Part 1: Code Review & Debugging

### Original Code Summary

The provided Flask API endpoint was designed to handle the creation of a new product and initialize its inventory. Although the code compiles, it has several design flaws that can lead to runtime failures and incorrect data handling.

### Identified Issues and Their Impacts

1. Missing input validation:

    * The code does not check if required fields like `name`, `sku`, `price`, etc., are present.
    * Impact: Missing fields could lead to KeyError exceptions and 500 server errors.

2. No SKU uniqueness enforcement:

    * There is no check to prevent duplicate SKUs.
    * Impact: Duplicate SKUs could cause confusion in product tracking and inventory reports.

3. Incorrect product-warehouse relationship:

    * The Product model incorrectly includes a `warehouse_id` field.
    * Impact: This limits the product to a single warehouse, which contradicts the requirement that a product can exist in multiple warehouses.

4. No transactional integrity:

    * If an error occurs while inserting inventory, the product record remains committed.
    * Impact: Results in inconsistent data with orphaned product entries.

5. Price is not explicitly cast:

    * No handling for data type conversion of the price field.
    * Impact: Can lead to incorrect data stored or conversion errors if the price input is not a float.

### Proposed Fix with Explanation

```python
@app.route('/api/products', methods=['POST'])
def create_product():
    data = request.get_json()
    required_fields = ['name', 'sku', 'price', 'warehouse_id', 'initial_quantity']
    for field in required_fields:
        if field not in data:
            return {"error": f"Missing field: {field}"}, 400

    existing = Product.query.filter_by(sku=data['sku']).first()
    if existing:
        return {"error": "SKU must be unique"}, 400

    try:
        with db.session.begin_nested():
            product = Product(
                name=data['name'],
                sku=data['sku'],
                price=float(data['price'])
            )
            db.session.add(product)
            db.session.flush()

            inventory = Inventory(
                product_id=product.id,
                warehouse_id=data['warehouse_id'],
                quantity=data['initial_quantity']
            )
            db.session.add(inventory)
        db.session.commit()

        return {"message": "Product created", "product_id": product.id}, 201

    except Exception as e:
        db.session.rollback()
        return {"error": "Failed to create product", "details": str(e)}, 500
```

This version adds input validation, transactional safety, uniqueness check, and corrects the product-warehouse association.

---

## Part 2: Database Design

### Schema Design Summary

The following tables were designed to capture the key relationships and operations for the inventory system:

* companies (id, name)
* warehouses (id, company\_id, name, location)
* products (id, name, sku, price, product\_type)
* suppliers (id, name, contact\_email)
* inventory (id, product\_id, warehouse\_id, quantity, threshold)
* product\_bundles (bundle\_id, component\_id, quantity)
* product\_suppliers (product\_id, supplier\_id)
* inventory\_history (id, inventory\_id, change\_amount, change\_type, changed\_at)

### Questions to the Product Team

1. Are bundled products automatically deducted from child inventories?
2. How is sales activity tracked and linked to inventory?
3. Should thresholds be global per product or specific per warehouse?
4. Can suppliers be linked to multiple companies?

### Design Justification

1. The schema is normalized to avoid redundancy and improve scalability.
2. SKU is set as a unique field with indexing for fast lookup.
3. Inventory table includes threshold per warehouse-product combination.
4. Bundles are modeled through a self-referencing join table.
5. Historical changes in inventory are tracked for audit and alerting purposes.

---

## Part 3: API Implementation

### Endpoint

GET /api/companies/{companyId}/alerts/low-stock

### Sample Response

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

### Implementation Highlights

* Built using Spring Boot and MySQL with Spring Data JPA.
* The inventory repository fetches all items below threshold for a specific company.
* The service layer maps data into DTOs for a clean API response.
* Optional supplier information is handled with null checks.
* Code is modular, testable, and adheres to good software practices.

---

## Assumptions

* The value for `daysUntilStockout` is hardcoded due to lack of sales tracking logic.
* Only the first supplier is shown per product for simplicity.
* Products may exist in multiple warehouses with separate stock levels.
* Authentication is not implemented as it wasn't required.
* Sales table and order management are out of scope.

---

Thank you for the opportunity to work on this case study.
