
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

## API Endpoints

- **GET /api/products**: List all products.
- **GET /api/products/{id}**: Get product details.
- **GET /api/products/{id}/history**: Get product details with modification history.
- **POST /api/products**: Add a new product.
- **DELETE /api/products/{id}**: Delete a product.
