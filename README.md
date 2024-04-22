# catalog-marketstore

## Sobre o Projeto

Este projeto consiste em uma API para cadastro de produtos em um site de vendas.

## Tecnologias Utilizadas

- [PostgreSQL](https://www.postgresql.org/)
- [Heroku](https://www.heroku.com/)
- [Spring Web](https://spring.io/projects/spring-framework)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)

## Documentação da API

Acesse a documentação da API utilizando o Swagger UI:

[Swagger UI](https://catalog-markertstore-f39518b90648.herokuapp.com/swagger-ui.html)

## Listagem de Produtos

![Listagem de Produtos](https://github.com/wandrad3/catalog-marketstore/assets/59511225/cecd7ea1-fc34-4a10-a3a3-fab524d60940)

## Diagrama UML

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
