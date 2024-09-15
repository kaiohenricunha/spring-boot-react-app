
# Aplicação Spring Boot com React

Esta é uma aplicação web full-stack usando Spring Boot no backend e React no frontend. O app permite gerenciar produtos através de APIs RESTful.

## Como Rodar

1. **Clonar o repositório:**

    ```bash
    git clone https://github.com/kaiohenricunha/spring-boot-react-app.git
    ```

2. **Executar com Docker Compose:**

    ```bash
    docker-compose up
    ```

3. **Testar com Swagger:**

    Acesse `http://localhost:8080/swagger-ui.html` para explorar e testar as APIs.

## Endpoints da API

- **GET /api/products**: Lista todos os produtos.
- **GET /api/products/{id}**: Detalhes de um produto.
- **POST /api/products**: Adiciona um novo produto.
- **DELETE /api/products/{id}**: Exclui um produto.
