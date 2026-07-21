# ChamoTech — TechAndes REST API

Catalog and order management backend for the tech store **TechAndes**. A Spring Boot + JPA REST API that manages customers, categories, tags, products and the order creation process, with stock control, frozen pricing and server-side total calculation.

## Stack

- Java 17
- Spring Boot 4.0.3 (Web, Data JPA, Validation)
- H2 (development, in-memory) / MySQL (deployment)
- JJWT + Spring Security Crypto (JWT auth, BCrypt)
- Lombok
- springdoc-openapi (Swagger)

## Running the project

Requires **JDK 17+** and Maven.

### Development (H2, default profile)

```bash
mvn spring-boot:run
```

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 console: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:chamotech`, user `sa`, no password)

The H2 database is recreated on every startup (`ddl-auto: create-drop`).

### Deployment (MySQL)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Environment variables (with defaults): `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION_MS`.

## Architecture (layers)

```
controller  -> receives HTTP, DTOs only, delegates to service
service     -> business rules and transactions
repository  -> data access (Spring Data JPA)
entity      -> JPA entities (never exposed)
dto         -> input/output objects
mapper      -> entity <-> DTO conversion (static, isolated)
security    -> JWT filter, role annotation and interceptor
exception   -> business exceptions + global @RestControllerAdvice
```

## Domain model

| Entity | Relationship |
|---|---|
| Customer | 1—N with Order |
| Order | N—1 with Customer; 1—N with OrderLine |
| Product | N—1 with Category; N—M with Tag |
| Category | 1—N with Product |
| Tag | N—M with Product |
| **OrderLine** | association entity Order↔Product with its own attributes: `quantity` and frozen `unitPrice` |

## Authentication and roles

Two roles: `ADMIN` and `USER`. Catalog reads (`GET` on customers, categories, tags, products) are public. Everything else requires a `Bearer` token issued by `/auth/login`.

- `POST /auth/register` — creates a `USER` account.
- `POST /auth/login` — returns a JWT with the user's role.
- Catalog writes (`POST`/`PUT`/`DELETE` on customers, categories, tags, products) and `GET /orders` require `ADMIN`.
- `POST /orders` requires any authenticated user (`ADMIN` or `USER`).

## Endpoints

Standard response: `{ success, message, data }`. Errors: `{ timestamp, status, message, path }`.

### Customers
- `GET /customers` — paginated list (`?page=&size=&sort=`)
- `GET /customers/{id}`
- `POST /customers` — 201, admin only
- `PUT /customers/{id}` — admin only
- `DELETE /customers/{id}` — admin only

### Categories
- `GET /categories`, `GET /categories/{id}`
- `POST /categories` — 201, admin only
- `PUT /categories/{id}` — admin only
- `DELETE /categories/{id}` — admin only, **409** if it has products associated

### Tags
- `GET /tags`, `GET /tags/{id}`
- `POST /tags` — 201, admin only
- `PUT /tags/{id}` — admin only
- `DELETE /tags/{id}` — admin only

### Products
- `GET /products` — paginated
- `GET /products/search?categoryId=&priceMin=&priceMax=&tag=` — combined optional filters
- `GET /products/{id}`
- `POST /products` — 201, admin only
- `PUT /products/{id}` — admin only
- `DELETE /products/{id}` — admin only, **409** if it is already part of an order

### Orders
- `GET /orders`, `GET /orders/{id}` — admin only
- `POST /orders` — **201**, authenticated. Creates the order: validates customer and products, checks stock (all-or-nothing), discounts inventory, freezes the unit price and calculates the total **on the server**.

## Status codes

| Code | When |
|---|---|
| 201 | resource created |
| 400 | input validation failed (Bean Validation) |
| 401 | missing or invalid token |
| 403 | authenticated but missing the required role |
| 404 | resource not found |
| 409 | business conflict (no stock, deleting a resource in use, duplicate name) |

## Design decisions

- **OrderLine as an association entity** (not `@ManyToMany`): it needs its own attributes (`quantity`, `unitPrice`).
- **Frozen price**: each line stores `unitPrice = product.price` at purchase time; later price changes never alter historical orders.
- **`@Transactional` on order creation**: the operation is all-or-nothing; if any product lacks stock, the whole transaction rolls back.
- **`BigDecimal`** for every monetary value (exact precision), never `double`.
- **DTOs on every boundary**: controllers never expose JPA entities.
- The **total is never trusted from the client**: the order request only carries `productId` + `quantity`.
- **Role check via a custom `@RequiresRole` annotation + `HandlerInterceptor`**, matching the JWT payload's role claim, instead of pulling in full Spring Security.

## Tests

Postman collection: [`ChamoTech.postman_collection.json`](./ChamoTech.postman_collection.json). Includes login/register, every CRUD endpoint and the full order creation flow, including the insufficient-stock (409) case.
