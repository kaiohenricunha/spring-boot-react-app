
# Inventory Service

The Inventory Service is a microservice responsible for managing product stock. It allows users to:

- Retrieve inventory data for all products or a specific product.
- Add or update product stock.
- Decrease product stock after sales or other operations.

## Features

- **GET** `/api/inventory` - Retrieve all inventory items.
- **GET** `/api/inventory/{productId}` - Retrieve inventory for a specific product by its ID.
- **POST** `/api/inventory/{productId}` - Update stock for a specific product.
- **POST** `/api/inventory/{productId}/decrease` - Decrease stock for a product by a certain quantity.

## Running and Testing the Service

### Pre-requisites

1. Clone the repository: `git clone git@github.com:kaiohenricunha/inventory-service.git`

2. A running Kubernetes cluster.

### Isolated Testing in Kubernetes

1. Deploy the service in Kubernetes:

    ```bash
    kubectl apply -f kubernetes-resources.yaml
    ```

2. Once the service is running, use `kubectl port-forward` to expose it locally:

    ```bash
    kubectl port-forward svc/inventory-service 8081:8081 -n springboot-react-app

    Forwarding from 127.0.0.1:8081 -> 8081
    Forwarding from [::1]:8081 -> 8081
    ```

3. Test the endpoints using `curl` commands:

    - **Get All Inventory**:

      ```bash
      curl -v -X GET "http://localhost:8081/api/inventory"
      ```

    - **Get Inventory by Product ID**:

      ```bash
      curl -v -X GET "http://localhost:8081/api/inventory/1"
      ```

    - **Update Product Stock**:

      ```bash
      curl -v -X POST "http://localhost:8081/api/inventory/1" -d "stock=100"
      ```

    - **Decrease Product Stock**:

      ```bash
      curl -v -X POST "http://localhost:8081/api/inventory/1/decrease" -d "quantity=10"
      ```

### Integration Testing with the Main Application

1. Deploy both the Inventory Service and the main application (Spring Boot + React App) in your Kubernetes cluster:

    ```bash
    kubectl apply -f kubernetes-resources.yaml
    ```

2. Ensure that the Inventory Service is reachable from the main app by verifying service discovery.

3. Test the integration using the main application's API, which interacts with the Inventory Service:

    ```bash
    kubectl port-forward svc/microservice1 8080:8080 -n springboot-react-app 
    Forwarding from 127.0.0.1:8080 -> 8080
    Forwarding from [::1]:8080 -> 8080
    ```

    ```bash
    curl -v -X POST "http://localhost:8080/api/products" -u user:password -H "Content-Type: application/json" -d '{"name": "Product A", "description": "Product A description", "price": 49.99}'
    ```

4. Get the product with its stock information (this triggers an API call to the Inventory Service):

    ```bash
    curl -v -X GET "http://localhost:8080/api/products/1" -u user:password
    ```

## Summary

The Inventory Service is a critical component that manages the stock of products. It is designed to be used in conjunction with a larger e-commerce or order management system. By exposing endpoints for stock management, the Inventory Service ensures that stock levels are always up-to-date.
