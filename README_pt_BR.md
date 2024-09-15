
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

## Integração com RabbitMQ

RabbitMQ é utilizado neste projeto para facilitar a comunicação entre microserviços. Especificamente, quando um produto é criado, atualizado ou deletado, uma mensagem é enviada ao RabbitMQ para atualizar o inventário em outro microserviço (`inventory-service`). As informações de inventário (como o ID do produto e o estoque) são transferidas através das filas do RabbitMQ, garantindo uma comunicação assíncrona e confiável entre os serviços.

## Endpoints da API

- **GET /api/products**: Lista todos os produtos.
- **GET /api/products/{id}**: Detalhes de um produto.
- **POST /api/products**: Adiciona um novo produto.
- **DELETE /api/products/{id}**: Exclui um produto.
