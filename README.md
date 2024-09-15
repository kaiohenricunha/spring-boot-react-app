
# Spring Boot React App

This is a full-stack web application built using Spring Boot for the microservice1 and React for the frontend. The microservice1 provides RESTful APIs to manage products, while the frontend offers a user interface to interact with these APIs. The microservice1 is configured to serve the frontend build files.

## Table of Contents

- [Spring Boot React App](#microservice1)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
  - [Running Tests and Viewing Results](#running-tests-and-viewing-results)
    - [Running Tests](#running-tests)
    - [Viewing test results](#viewing-test-results)
  - [Usage](#usage)
    - [API Endpoints](#api-endpoints)
    - [Login Credentials](#login-credentials)
    - [Persistence Layer Design](#persistence-layer-design)
    - [Data Models](#data-models)
    - [Repository Usage](#repository-usage)
    - [Persistence Layer Example](#persistence-layer-example)
      - [Example: Creating and Saving a Product](#example-creating-and-saving-a-product)
      - [Example: Updating a Product and Saving History](#example-updating-a-product-and-saving-history)
      - [Example: Fetching Product History](#example-fetching-product-history)
    - [Conclusion](#conclusion)

## Features

- **Product Management**: Add, view, and delete products.
- **RESTful APIs**: microservice1 provides a set of APIs to manage products.
- **Frontend Serving**: microservice1 is configured to serve the frontend React app.

## Prerequisites

- **Java 17**: Ensure you have JDK 17 installed.
- **Node.js & npm**: Install Node.js (which includes npm) for building the frontend.
- **Gradle**: Ensure you have Gradle installed for building and running the Spring Boot application.

## Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/yourusername/microservice1.git
    cd microservice1
    ```

## Running the Application

1. **Build the Application**

   Navigate to the root of the project directory and run:

   ```bash
   ./gradlew clean build
   ```

   This command will:
   - Build the React frontend and place the production files in the `microservice1/src/main/resources/static` directory.
   - Build the Spring Boot microservice1, which will automatically serve the frontend build files.

2. **Run the microservice1 with the Frontend**

   After the build is successful, you can start the microservice1 which will also serve the frontend:

   ```bash
   java -jar microservice1/build/libs/microservice1-0.0.1-SNAPSHOT.jar
   ```

   This will start the Spring Boot application on `http://localhost:8080`. The React frontend will be served from the microservice1 automatically.

## Running Tests and Viewing Results

This project includes unit and integration tests to ensure the correctness of the application. The tests are written using JUnit and Mockito for the microservice1.

### Running Tests

To run all tests, use the following command from the root of your project:

```bash
./gradlew test
```

This command will execute all tests in the microservice1 module, including unit tests and integration tests.

### Viewing test results

After running the tests, you can view a detailed report of the test results. The test results are automatically generated and stored in the following location:

`microservice1/build/reports/tests/test/index.html`.

## Usage

- **Access the Application**: Open the browser and navigate to `http://localhost:8080`. You will see the React frontend.
- **View Products**: The homepage will display a list of products.
- **Add a Product**: Use the form to add a new product, including its name, description, and price.
- **Delete a Product**: Each product in the list will have a delete button to remove it.

### API Endpoints

The microservice1 exposes the following RESTful endpoints:

- **GET /api/products**: Get all products.
- **GET /api/products/{id}**: Get a product by its ID.
- **POST /api/products**: Create a new product.
- **DELETE /api/products/{id}**: Delete a product by its ID.

### Login Credentials

If Spring Security is enabled, you may have a default generated password. The default username is `user`, and the password is printed in the console when the application starts:

```bash
Using generated security password: <your-generated-password>
```

### Persistence Layer Design

The persistence layer is responsible for interacting with the database to perform CRUD (Create, Read, Update, Delete) operations. In this application, the persistence layer is implemented using **Spring Data JPA** to abstract away the complexities of database interactions.

### Data Models

The application uses two main entities: `Product` and `ProductHistory`.

1. **Product Entity**

   The `Product` entity represents an item with attributes such as `id`, `name`, `description`, and `price`. It also supports optimistic locking via the `@Version` annotation to handle concurrent updates.

   ```java
   @Entity
   public class Product {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       private String name;
       private String description;
       private Double price;

       @Version
       private Long version;

       // Constructors, getters, and setters
   }
   ```

   - `@Entity`: Marks this class as a JPA entity mapped to the `Product` table.
   - `@Id`: Specifies the primary key.
   - `@GeneratedValue`: Configures the generation strategy for the primary key.
   - `@Version`: Used for optimistic locking to ensure data integrity during concurrent transactions.

2. **ProductHistory Entity**

   The `ProductHistory` entity stores the historical records of changes made to `Product`. It captures details like the previous state of the product, who modified it, and when.

   ```java
   @Entity
   public class ProductHistory {
       @Id
       @GeneratedValue(strategy = GenerationType.IDENTITY)
       private Long id;
       private Long productId;
       private String name;
       private String description;
       private Double price;
       private String modifiedBy;
       private LocalDateTime modifiedDate;

       // Constructors, getters, and setters
   }
   ```

   - This entity tracks changes for audit purposes and is saved every time a `Product` is updated.

### Repository Usage

The persistence layer uses **Spring Data JPA Repositories** to simplify database interactions. The `ProductRepository` and `ProductHistoryRepository` extend the `JpaRepository` interface, which provides CRUD methods out of the box.

1. **ProductRepository**

   ```java
   public interface ProductRepository extends JpaRepository<Product, Long> {
       // Additional custom queries can be defined here if needed
   }
   ```

   This repository is used for basic product operations such as:
   - `findAll()`: Retrieve all products.
   - `findById(Long id)`: Retrieve a specific product by its ID.
   - `save(Product product)`: Save or update a product.
   - `deleteById(Long id)`: Delete a product by its ID.

2. **ProductHistoryRepository**

   ```java
   public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
       Page<ProductHistory> findByProductId(Long productId, Pageable pageable);
   }
   ```

   This repository is used to retrieve the history of changes made to a product:
   - `findByProductId(Long productId, Pageable pageable)`: Retrieve paginated history records for a specific product.

### Persistence Layer Example

#### Example: Creating and Saving a Product

The service layer uses the repository to perform database operations. Here’s how you can create and save a new product:

```java
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
}
```

To save a product, call the service from the controller:

```java
@PostMapping
public Product createProduct(@RequestBody Product product) {
    return productService.createProduct(product);
}
```

#### Example: Updating a Product and Saving History

Every time a product is updated, its previous state is saved in the `ProductHistory` entity to allow for auditing and traceability.

```java
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductHistoryRepository productHistoryRepository;

    public Product updateProduct(Long id, Product updatedProduct, String modifiedBy) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Save the history of the product before updating
        ProductHistory history = new ProductHistory(
            existingProduct.getId(),
            existingProduct.getName(),
            existingProduct.getDescription(),
            existingProduct.getPrice(),
            modifiedBy,
            LocalDateTime.now()
        );
        productHistoryRepository.save(history);

        // Update the product and save the changes
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());

        return productRepository.save(existingProduct);
    }
}
```

This ensures that the `ProductHistory` table will have a record of all changes made to a product.

#### Example: Fetching Product History

The history of changes can be retrieved for a product by calling the `ProductHistoryRepository`:

```java
@GetMapping("/{id}/history")
public Page<ProductHistory> getProductHistory(@PathVariable Long id, Pageable pageable) {
    return productService.getProductHistory(id, pageable);
}
```

This provides a paginated response of the historical changes for a specific product.

### Conclusion

The persistence layer in this application leverages **Spring Data JPA** to manage database operations for `Product` and `ProductHistory`. By using JPA annotations for object-relational mapping and the repository pattern, the application simplifies data access and management. Additionally, the integration of product history tracking allows for detailed auditing of product changes, ensuring traceability and data integrity.
