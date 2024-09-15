
# Spring Boot React App

This is a full-stack web application using Spring Boot for the backend and React for the frontend. The app allows managing products via RESTful APIs.

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

## API Endpoints

- **GET /api/products**: List all products.
- **GET /api/products/{id}**: Get product details.
- **POST /api/products**: Add a new product.
- **DELETE /api/products/{id}**: Delete a product.
