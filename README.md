# ChamoTech — TechAndes REST API

Catalog and order management backend for the tech store **TechAndes**.

## Running the project

Requires **JDK 17+** and Maven.

### Development (H2, default profile)

```bash
mvn spring-boot:run
```

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- H2 console: `http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:chamotech`, user `sa`, no password)

### Deployment (MySQL)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

Environment variables (with defaults): `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `JWT_EXPIRATION_MS`.

## Endpoints

Standard response: `{ success, message, data }`. Errors: `{ timestamp, status, message, path }`.

### Auth
- `POST /auth/register`
- `POST /auth/login`

### Customers
- `GET /customers`
- `GET /customers/{id}`
- `POST /customers` — admin only
- `PUT /customers/{id}` — admin only
- `DELETE /customers/{id}` — admin only

### Categories
- `GET /categories`
- `GET /categories/{id}`
- `POST /categories` — admin only
- `PUT /categories/{id}` — admin only
- `DELETE /categories/{id}` — admin only

### Tags
- `GET /tags`
- `GET /tags/{id}`
- `POST /tags` — admin only
- `PUT /tags/{id}` — admin only
- `DELETE /tags/{id}` — admin only

### Products
- `GET /products`
- `GET /products/search?categoryId=&priceMin=&priceMax=&tag=`
- `GET /products/{id}`
- `POST /products` — admin only
- `PUT /products/{id}` — admin only
- `DELETE /products/{id}` — admin only

### Orders
- `GET /orders` — admin only
- `GET /orders/{id}` — admin only
- `POST /orders` — authenticated
