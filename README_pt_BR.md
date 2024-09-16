
# Aplicação Spring Boot

Esta é uma aplicação web usando Spring Boot para o backend. A aplicação permite gerenciar produtos via APIs RESTful.

## Como Executar

1. **Clonar o repositório:**

    ```bash
    git clone https://github.com/kaiohenricunha/spring-boot-react-app.git
    ```

2. **Executar com Docker Compose:**

    ```bash
    docker-compose up
    ```

3. **Testar com Swagger:**

    Vá para `http://localhost:8080/swagger-ui.html` para explorar e testar a API.

## Integração com RabbitMQ

O RabbitMQ é utilizado neste projeto para facilitar a comunicação entre microserviços. Especificamente, quando um produto é criado, atualizado ou excluído, uma mensagem é enviada ao RabbitMQ para atualizar o estoque em outro microserviço (`inventory-service`). As informações de inventário (como o ID do produto e o estoque) são transferidas através das filas do RabbitMQ, garantindo comunicação assíncrona e confiável entre os serviços.

## Diagrama de Arquitetura

+---------------------+       +--------------------+       +--------------------+
|  Frontend (React)    | <---> |  Backend API       | <---> |  Broker RabbitMQ    |
|  (Interface de Usuário) |       |  (Spring Boot)     |       |  (Fila de Mensagens) |
+---------------------+       +--------------------+       +--------------------+
                                  |         ^
                                  |         |
                                  v         |
+--------------------+       +--------------------+       +--------------------+
|  Serviço de Produto | <---> |  Serviço de Inventário | <---> |  Fila RabbitMQ   |
|  (Microserviço 1)  |       |  (Microserviço 2)   |       |  (inventory_queue) |
+--------------------+       +--------------------+       +--------------------+

## Fluxo de Eventos

### Criação/Atualização/Exclusão de Produto

- Um usuário interage com o aplicativo frontend em React para criar, atualizar ou excluir um produto.
- O frontend envia uma requisição HTTP para o backend (`ProductService`) no Spring Boot.

### Processamento no Serviço de Produto (Microserviço 1)

- O `ProductService` processa a requisição e atualiza os dados do produto no banco de dados.
- Após atualizar os dados do produto, o `ProductService` cria uma mensagem contendo as informações do produto (como o ID do produto e o estoque) e envia essa mensagem para o RabbitMQ através da fila `inventory_queue` usando a classe `Producer`.

### RabbitMQ

- O RabbitMQ atua como broker de mensagens e mantém a mensagem na fila `inventory_queue`.
- A mensagem é entregue de forma assíncrona para qualquer serviço que esteja escutando essa fila, garantindo comunicação confiável, mesmo que o serviço receptor esteja temporariamente indisponível.

### Serviço de Inventário (Microserviço 2)

- O `InventoryService` escuta a fila `inventory_queue` no RabbitMQ.
- Quando uma mensagem chega, o `InventoryService` consome a mensagem, atualiza seu inventário interno com base nas informações de ID do produto e estoque, e salva esse inventário atualizado em seu próprio banco de dados.

### Resultado

- O inventário no `InventoryService` é atualizado com base nas mudanças feitas no `ProductService`.
- Isso garante que as atualizações de produtos em um microserviço (`Product Service`) sejam refletidas em outro microserviço (`Inventory Service`) de forma assíncrona e confiável por meio do RabbitMQ.

## Endpoints da API

- **GET /api/products**: Lista todos os produtos.
- **GET /api/products/{id}**: Obtém os detalhes de um produto.
- **GET /api/products/{id}/history**: Obtém os detalhes de um produto com histórico de modificações.
- **POST /api/products**: Adiciona um novo produto.
- **DELETE /api/products/{id}**: Exclui um produto.

## Endpoints Adicionais

- **Painel do Eureka Server** (`http://localhost:8761`): Esta é a interface de gerenciamento do Eureka Server. Exibe todos os microserviços registrados, suas instâncias e seus status. O Eureka é usado para descoberta de serviços, permitindo que microserviços se encontrem e se comuniquem dinamicamente.

- **Gerenciamento do RabbitMQ** (`http://localhost:15672`): Esta é a interface de gerenciamento do RabbitMQ. Fornece um painel visual para monitorar filas, exchanges e mensagens, bem como gerenciar as configurações do RabbitMQ, como usuários, conexões e canais.
