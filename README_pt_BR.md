# Aplicação Spring Boot

Esta é uma aplicação web utilizando Spring Boot para o backend. A aplicação permite gerenciar produtos via APIs RESTful.

## Como Executar

1. **Clone o repositório:**

    ```bash
    git clone https://github.com/kaiohenricunha/spring-boot-react-app.git
    ```

2. **Execute com Docker Compose:**

    ```bash
    docker-compose up
    ```

3. **Teste com Swagger:**

    Vá para `http://localhost:8080/swagger-ui.html` para explorar e testar a API.

## Integração com RabbitMQ

RabbitMQ é usado neste projeto para facilitar a comunicação entre microserviços. Especificamente, quando um produto é criado, atualizado ou excluído, uma mensagem é enviada ao RabbitMQ para atualizar o estoque em outro microserviço (`inventory-service`). As informações de inventário (como ID do produto e estoque) são transferidas via filas do RabbitMQ, garantindo comunicação assíncrona e confiável entre os serviços.

## Logging e Tracing

Esta aplicação está instrumentada com **OpenTelemetry** para tracing distribuído e logging. Logs e traces são usados para monitorar o fluxo de requisições entre microserviços e são exportados automaticamente para o Jaeger para tracing e Loki para logging.

### Logging

- **Loki** é utilizado como backend de logging. Logs dos serviços são coletados pelo **Fluent Bit** e enviados para o **Loki**.
- O OpenTelemetry auto-instrumenta os logs para incluir informações contextuais como `trace_id` e `span_id`, facilitando a correlação entre entradas de log e traces.

### Tracing com Jaeger

- **Jaeger** é usado como backend de tracing. A aplicação exporta traces via OpenTelemetry, e esses traces podem ser visualizados na interface do Jaeger.
- Cada microserviço está configurado para enviar seus traces para o Jaeger utilizando a auto-instrumentação do OpenTelemetry.
- Acesse o Jaeger em `http://localhost:16686` para explorar traces distribuídos e analisar o fluxo de requisições entre microserviços.

## Diagrama de Arquitetura

+---------------------+       +--------------------+       +--------------------+
|  Frontend (React)    | <---> |  Backend API       | <---> |  Broker RabbitMQ    |
|  (Interface Usuário) |       |  (Spring Boot)     |       |  (Fila de Mensagens)|
+---------------------+       +--------------------+       +--------------------+
                                  |         ^
                                  |         |
                                  v         |
+--------------------+       +--------------------+       +--------------------+
|  Serviço Produto   | <---> |  Serviço Inventário | <---> |  Fila RabbitMQ      |
|  (Microserviço 1)  |       |  (Microserviço 2)   |       |  (inventory_queue)  |
+--------------------+       +--------------------+       +--------------------+

## Fluxo de Eventos

### Criação/Atualização/Exclusão de Produtos

- Um usuário interage com o aplicativo frontend em React para criar, atualizar ou excluir um produto.
- O frontend envia uma requisição HTTP para o backend (`ProductService`) no Spring Boot.

### Processamento no Product Service (Microserviço 1)

- O `ProductService` processa a requisição e atualiza os dados do produto no banco de dados.
- Após atualizar os dados do produto, o `ProductService` cria uma mensagem contendo as informações do produto (como ID do produto e estoque) e envia essa mensagem para o RabbitMQ via a fila `inventory_queue` utilizando a classe `Producer`.

### RabbitMQ

- O RabbitMQ atua como o broker de mensagens e mantém a mensagem na fila `inventory_queue`.
- A mensagem é entregue de forma assíncrona para qualquer serviço que esteja ouvindo essa fila, garantindo comunicação confiável mesmo que o serviço receptor esteja temporariamente fora do ar.

### Inventory Service (Microserviço 2)

- O `InventoryService` escuta a fila `inventory_queue` no RabbitMQ.
- Quando uma mensagem chega, o `InventoryService` consome a mensagem, atualiza seu inventário interno com base no ID do produto e nas informações de estoque, e salva esse inventário atualizado no seu próprio banco de dados.

### Resultado

- O inventário no `InventoryService` é atualizado com base nas alterações feitas no produto no `ProductService`.
- Isso garante que as atualizações de produtos em um microserviço (`Product Service`) sejam refletidas em outro microserviço (`Inventory Service`) de forma assíncrona e confiável através do RabbitMQ.

## Endpoints da API

- **GET /api/products**: Lista todos os produtos.
- **GET /api/products/{id}**: Obtém os detalhes de um produto.
- **GET /api/products/{id}/history**: Obtém o histórico de modificações de um produto.
- **POST /api/products**: Adiciona um novo produto.
- **DELETE /api/products/{id}**: Exclui um produto.

## Endpoints Adicionais

- **Dashboard do Eureka Server** (`http://localhost:8761`): Esta é a interface de gerenciamento do Eureka Server. Ela exibe todos os microserviços registrados, suas instâncias e seus status. O Eureka é utilizado para descoberta de serviços, permitindo que os microserviços se encontrem e comuniquem de forma dinâmica.

- **Gerenciamento do RabbitMQ** (`http://localhost:15672`): Esta é a interface de gerenciamento do RabbitMQ. Ela fornece um dashboard visual para monitorar filas, exchanges e mensagens, além de gerenciar configurações do RabbitMQ como usuários, conexões e canais.

- **Interface do Jaeger** (`http://localhost:16686`): O Jaeger fornece uma interface de tracing distribuído.

- **Dashboard do Grafana** (`http://localhost:3000`): O Grafana fornece uma interface visual para explorar métricas e logs. Em "Explore" é possível visualizar os logs de todos os containeres da aplicação.
