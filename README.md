# Catalog MarketStore

Este é um aplicativo de API para cadastro de produtos em um site de vendas. Ele utiliza PostgreSQL como banco de dados, é hospedado no Heroku e foi desenvolvido com Spring Web e Spring Data JPA.

## Acesso à API

Você pode acessar a API em: [https://catalog-markertstore-f39518b90648.herokuapp.com/swagger-ui.html](https://catalog-markertstore-f39518b90648.herokuapp.com/swagger-ui.html)

## Capturas de Tela

### Listagem de Produtos
![Listagem de Produtos](https://github.com/wandrad3/catalog-marketstore/assets/59511225/cecd7ea1-fc34-4a10-a3a3-fab524d60940)

### Detalhes do Produto
![Detalhes do Produto](https://github.com/wandrad3/catalog-marketstore/assets/59511225/488b87bd-f899-48fe-bf83-72cf5d7dd882)

## Diagrama UML

Aqui está o diagrama de classes UML representando a estrutura do modelo de dados utilizado na API:

```mermaid
classDiagram
    class Product {
        -int id
        -String name
        -String description
        -String imgUrl
        -double price
        -Date date
        -Category[] categories
    }

    class Category {
        -int id
        -String name
    }

    Product "1" -- "N" Category
