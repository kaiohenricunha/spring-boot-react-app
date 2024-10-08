# Spring Boot App

This is a web application using Spring Boot for the backend. The app allows managing products via RESTful APIs.

## How to Run

1. **Clone the repository:**

    ```bash
    git clone https://github.com/kaiohenricunha/spring-boot-react-app.git
    ```

2. **Run with Docker Compose:**

    ```bash
    docker-compose up
    ```

3. **Test with Swagger:**

    Go to `http://localhost:8080/swagger-ui.html` to explore and test the API.

## RabbitMQ Integration

RabbitMQ is used in this project to facilitate communication between microservices. Specifically, when a product is created, updated, or deleted, a message is sent to RabbitMQ to update the inventory in another microservice (`inventory-service`). The inventory information (such as product ID and stock) is transferred via RabbitMQ queues, ensuring asynchronous and reliable communication between services.

## Logging and Tracing

This application is instrumented with **OpenTelemetry** for distributed tracing and logging. Logs and traces can be used to monitor the flow of requests across microservices and are automatically exported to Jaeger for tracing and Loki for logging.

### Logging

- **Loki** is used as the logging backend. Logs from the services are collected by **Fluent Bit** and pushed to **Loki**.
- OpenTelemetry auto-instruments logging to include contextual information like `trace_id` and `span_id`, making it easier to correlate log entries with traces.

### Tracing with Jaeger

- **Jaeger** is used as the tracing backend. The application exports traces via OpenTelemetry, and these traces can be visualized in Jaeger’s UI.
- Each microservice is configured to send its traces to Jaeger using OpenTelemetry’s auto-instrumentation.
- Visit Jaeger at `http://localhost:16686` to explore distributed traces and analyze request flows across microservices.

## Architecture Diagram

+---------------------+       +--------------------+       +--------------------+
|  Frontend (React)    | <---> |  Backend API       | <---> |  RabbitMQ Broker    |
|  (User Interface)    |       |  (Spring Boot)     |       |  (Message Queue)    |
+---------------------+       +--------------------+       +--------------------+
                                  |         ^
                                  |         |
                                  v         |
+--------------------+       +--------------------+       +--------------------+
|  Product Service   | <---> |  Inventory Service  | <---> |  RabbitMQ Queue    |
|  (Microservice 1)  |       |  (Microservice 2)   |       |  (inventory_queue) |
+--------------------+       +--------------------+       +--------------------+

## Event Flow

### Product Creation/Update/Deletion

- A user interacts with the frontend React app to create, update, or delete a product.
- The frontend sends an HTTP request to the backend (`ProductService`) in Spring Boot.

### Processing in Product Service (Microservice 1)

- The `ProductService` processes the request and updates the product data in the database.
- After updating the product data, the `ProductService` creates a message containing the product information (such as product ID and stock) and sends this message to RabbitMQ via the `inventory_queue` using the `Producer` class.

### RabbitMQ

- RabbitMQ acts as the message broker and holds the message in the `inventory_queue`.
- The message is asynchronously delivered to any service listening to this queue, ensuring reliable communication even if the receiving service is temporarily down.

### Inventory Service (Microservice 2)

- The `InventoryService` listens to the `inventory_queue` in RabbitMQ.
- When a message arrives, `InventoryService` consumes the message, updates its internal inventory based on the product ID and stock information, and saves this updated inventory to its own database.

### Result

- The inventory in `InventoryService` is updated based on the changes made to the product in `ProductService`.
- This ensures that product updates in one microservice (`Product Service`) are reflected in another microservice (`Inventory Service`) asynchronously and reliably through RabbitMQ.

## API Endpoints

- **GET /api/products**: List all products.
- **GET /api/products/{id}**: Get product details.
- **GET /api/products/{id}/history**: Get product details with modification history.
- **POST /api/products**: Add a new product.
- **DELETE /api/products/{id}**: Delete a product.

## Additional Endpoints

- **Eureka Server Dashboard** (`http://localhost:8761`): This is the Eureka Server management interface. It displays all the registered microservices, their instances, and their statuses. Eureka is used for service discovery, allowing microservices to find and communicate with each other dynamically.

- **RabbitMQ Management** (`http://localhost:15672`): This is the RabbitMQ management interface. It provides a visual dashboard to monitor queues, exchanges, and messages, as well as manage RabbitMQ settings such as users, connections, and channels.

- **Jaeger UI** (`http://localhost:16686`): Jaeger provides an interface for distributed tracing, which can use it to analyze request traces and understand how requests flow between microservices.

- **Grafana Dashboard** (`http://localhost:3000`): Grafana provides a visual interface for exploring metrics and logs. The Loki datasource is used to display the logs collected from the microservices on the "Explore" section.
