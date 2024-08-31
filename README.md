
# Spring Boot React App

This is a full-stack web application built using Spring Boot for the backend and React for the frontend. The backend provides RESTful APIs to manage products, while the frontend offers a user interface to interact with these APIs. The backend is configured to serve the frontend build files.

## Table of Contents

- [Spring Boot React App](#spring-boot-react-app)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
  - [Usage](#usage)
    - [API Endpoints](#api-endpoints)
    - [Login Credentials](#login-credentials)

## Features

- **Product Management**: Add, view, and delete products.
- **RESTful APIs**: Backend provides a set of APIs to manage products.
- **Frontend Serving**: Backend is configured to serve the frontend React app.

## Prerequisites

- **Java 17**: Ensure you have JDK 17 installed.
- **Node.js & npm**: Install Node.js (which includes npm) for building the frontend.
- **Gradle**: Ensure you have Gradle installed for building and running the Spring Boot application.

## Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/yourusername/spring-boot-react-app.git
    cd spring-boot-react-app
    ```

## Running the Application

1. **Build the Application**

   Navigate to the root of your project directory and run:

   ```bash
   ./gradlew clean build
   ```

   This command will:
   - Build the React frontend and place the production files in the `backend/src/main/resources/static` directory.
   - Build the Spring Boot backend, which will automatically serve the frontend build files.

2. **Run the Backend with the Frontend**

   After the build is successful, you can start the backend which will also serve the frontend:

   ```bash
   java -jar backend/build/libs/backend-0.0.1-SNAPSHOT.jar
   ```

   This will start the Spring Boot application on `http://localhost:8080`. The React frontend will be served from the backend automatically.

## Usage

- **Access the Application**: Open your browser and navigate to `http://localhost:8080`. You will see the React frontend.
- **View Products**: The homepage will display a list of products.
- **Add a Product**: Use the form to add a new product, including its name, description, and price.
- **Delete a Product**: Each product in the list will have a delete button to remove it.

### API Endpoints

The backend exposes the following RESTful endpoints:

- **GET /api/products**: Get all products.
- **GET /api/products/{id}**: Get a product by its ID.
- **POST /api/products**: Create a new product.
- **DELETE /api/products/{id}**: Delete a product by its ID.

### Login Credentials

If Spring Security is enabled, you may have a default generated password. The default username is `user`, and the password is printed in the console when the application starts:

```bash
Using generated security password: <your-generated-password>
```
