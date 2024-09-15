
# Aplicação Spring Boot com React

Este é um aplicativo web full-stack construído usando Spring Boot para o microservice1 e React para o frontend. O microservice1 fornece APIs RESTful para gerenciar produtos, enquanto o frontend oferece uma interface de usuário para interagir com essas APIs. O microservice1 está configurado para servir os arquivos de build do frontend.

## Índice

- [Aplicação Spring Boot com React](#aplicação-spring-boot-com-react)
  - [Índice](#índice)
  - [Funcionalidades](#funcionalidades)
  - [Pré-requisitos](#pré-requisitos)
  - [Instalação](#instalação)
  - [Executando a Aplicação](#executando-a-aplicação)
  - [Executando Testes e Visualizando Resultados](#executando-testes-e-visualizando-resultados)
    - [Executando Testes](#executando-testes)
    - [Visualizando Resultados dos Testes](#visualizando-resultados-dos-testes)
  - [Uso](#uso)
    - [Endpoints da API](#endpoints-da-api)
    - [Credenciais de Login](#credenciais-de-login)
    - [Design da Camada de Persistência](#design-da-camada-de-persistência)
      - [Modelos de Dados](#modelos-de-dados)
      - [Uso do Repositório](#uso-do-repositório)
      - [Exemplo da Camada de Persistência](#exemplo-da-camada-de-persistência)
        - [Exemplo: Criando e Salvando um Produto](#exemplo-criando-e-salvando-um-produto)
        - [Exemplo: Atualizando um Produto e Salvando o Histórico](#exemplo-atualizando-um-produto-e-salvando-o-histórico)
        - [Exemplo: Buscando Histórico de Produtos](#exemplo-buscando-histórico-de-produtos)
    - [Conclusão](#conclusão)

## Funcionalidades

- **Gerenciamento de Produtos**: Adicionar, visualizar e excluir produtos.
- **APIs RESTful**: microservice1 fornece um conjunto de APIs para gerenciar produtos.
- **Serviço de Frontend**: microservice1 está configurado para servir o app React no frontend.

## Pré-requisitos

- **Java 17**: Certifique-se de ter o JDK 17 instalado.
- **Node.js & npm**: Instale Node.js (que inclui npm) para construir o frontend.
- **Gradle**: Certifique-se de ter o Gradle instalado para construir e executar a aplicação Spring Boot.

## Instalação

1. **Clone o repositório:**

    ```bash
    git clone https://github.com/seu-usuario/microservice1.git
    cd microservice1
    ```

2. **Construa o projeto:**

    - Para o microservice1 (Spring Boot):

      ```bash
      ./gradlew build
      ```

    - Para o frontend (React):

      ```bash
      cd frontend
      npm install
      npm run build
      ```

3. **Execute o microservice1:**

    ```bash
    ./gradlew bootRun
    ```

    O Spring Boot irá servir o aplicativo React como parte da aplicação microservice1.

## Executando a Aplicação

1. Acesse `http://localhost:8080` no navegador para visualizar a aplicação.
2. As APIs RESTful podem ser acessadas via `http://localhost:8080/api`.

## Executando Testes e Visualizando Resultados

### Executando Testes

Para rodar testes automatizados, use o seguinte comando no microservice1:

```bash
./gradlew test
```

### Visualizando Resultados dos Testes

Os resultados dos testes estarão disponíveis no diretório `build/reports/tests/test/index.html`.

## Uso

### Endpoints da API

- **GET** `/api/products`: Retorna a lista de todos os produtos.
- **POST** `/api/products`: Cria um novo produto.
- **GET** `/api/products/{id}`: Retorna detalhes de um produto específico.
- **PUT** `/api/products/{id}`: Atualiza um produto.
- **DELETE** `/api/products/{id}`: Exclui um produto.

### Credenciais de Login

- **Usuário**: admin
- **Senha**: admin

### Design da Camada de Persistência

A camada de persistência foi projetada usando Spring Data JPA para gerenciar as operações de banco de dados dos modelos `Product` e `ProductHistory`.

#### Modelos de Dados

- **Produto**: Representa os produtos na loja.
- **Histórico de Produto**: Mantém o registro das mudanças de cada produto.

#### Uso do Repositório

Os repositórios Spring Data JPA facilitam o acesso aos dados do banco de dados.

#### Exemplo da Camada de Persistência

##### Exemplo: Criando e Salvando um Produto

```java
Product newProduct = new Product("Nome do Produto", "Descrição", 100.00);
productRepository.save(newProduct);
```

##### Exemplo: Atualizando um Produto e Salvando o Histórico

```java
@Autowired
private ProductRepository productRepository;

@Autowired
private ProductHistoryRepository productHistoryRepository;

public Product updateProduct(Long id, Product updatedProduct, String modifiedBy) {
    Product existingProduct = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

    ProductHistory history = new ProductHistory(
        existingProduct.getId(),
        existingProduct.getName(),
        existingProduct.getDescription(),
        existingProduct.getPrice(),
        modifiedBy,
        LocalDateTime.now()
    );
    productHistoryRepository.save(history);

    existingProduct.setName(updatedProduct.getName());
    existingProduct.setDescription(updatedProduct.getDescription());
    existingProduct.setPrice(updatedProduct.getPrice());

    return productRepository.save(existingProduct);
}
```

##### Exemplo: Buscando Histórico de Produtos

```java
@GetMapping("/{id}/history")
public Page<ProductHistory> getProductHistory(@PathVariable Long id, Pageable pageable) {
    return productService.getProductHistory(id, pageable);
}
```

### Conclusão

A camada de persistência nesta aplicação utiliza **Spring Data JPA** para gerenciar as operações de banco de dados de `Product` e `ProductHistory`. O rastreamento do histórico de produtos permite auditoria detalhada das mudanças feitas nos produtos.
