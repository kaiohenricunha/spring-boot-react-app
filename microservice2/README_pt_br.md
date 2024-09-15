# Serviço de Inventário

O Serviço de Inventário é um microsserviço responsável por gerenciar o estoque de produtos. Ele permite aos usuários:

- Recuperar dados de inventário de todos os produtos ou de um produto específico.
- Adicionar ou atualizar o estoque de produtos.
- Reduzir o estoque de produtos após vendas ou outras operações.

## Funcionalidades

- **GET** `/api/inventory` - Recuperar todos os itens de inventário.
- **GET** `/api/inventory/{productId}` - Recuperar o inventário de um produto específico pelo seu ID.
- **POST** `/api/inventory/{productId}` - Atualizar o estoque de um produto específico.
- **POST** `/api/inventory/{productId}/decrease` - Reduzir o estoque de um produto por uma quantidade específica.

## Executando e Testando o Serviço

### Pré-requisitos

1. Clone o repositório: `git clone git@github.com:kaiohenricunha/inventory-service.git`

2. Um cluster Kubernetes em execução.

### Teste Isolado no Kubernetes

1. Faça o deploy do serviço no Kubernetes:

    ```bash
    kubectl apply -f kubernetes-resources.yaml
    ```

2. Assim que o serviço estiver em execução, use o `kubectl port-forward` para expô-lo localmente:

    ```bash
    kubectl port-forward svc/inventory-service 8081:8081 -n springboot-react-app

    Forwarding from 127.0.0.1:8081 -> 8081
    Forwarding from [::1]:8081 -> 8081
    ```

3. Teste os endpoints usando comandos `curl`:

    - **Obter Todo o Inventário**:

      ```bash
      curl -v -X GET "http://localhost:8081/api/inventory"
      ```

    - **Obter Inventário por ID do Produto**:

      ```bash
      curl -v -X GET "http://localhost:8081/api/inventory/1"
      ```

    - **Atualizar Estoque do Produto**:

      ```bash
      curl -v -X POST "http://localhost:8081/api/inventory/1" -d "stock=100"
      ```

    - **Reduzir Estoque do Produto**:

      ```bash
      curl -v -X POST "http://localhost:8081/api/inventory/1/decrease" -d "quantity=10"
      ```

### Teste de Integração com a Aplicação Principal

1. Faça o deploy tanto do Serviço de Inventário quanto da aplicação principal (Spring Boot + React App) no seu cluster Kubernetes:

    ```bash
    kubectl apply -f kubernetes-resources.yaml
    ```

2. Certifique-se de que o Serviço de Inventário está acessível pela aplicação principal verificando a descoberta de serviços.

3. Teste a integração usando a API da aplicação principal, que interage com o Serviço de Inventário:

    ```bash
    kubectl port-forward svc/microservice1 8080:8080 -n springboot-react-app 
    Forwarding from 127.0.0.1:8080 -> 8080
    Forwarding from [::1]:8080 -> 8080
    ```

    ```bash
    curl -v -X POST "http://localhost:8080/api/products" -u user:password -H "Content-Type: application/json" -d '{"name": "Product A", "description": "Descrição do Produto A", "price": 49.99}'
    ```

4. Obtenha o produto com suas informações de estoque (isso dispara uma chamada de API para o Serviço de Inventário):

    ```bash
    curl -v -X GET "http://localhost:8080/api/products/1" -u user:password
    ```

## Resumo

O Serviço de Inventário é um componente crítico que gerencia o estoque de produtos. Ele foi projetado para ser usado em conjunto com um sistema maior de e-commerce ou de gestão de pedidos. Ao expor endpoints para o gerenciamento de estoque, o Serviço de Inventário garante que os níveis de estoque estejam sempre atualizados.
