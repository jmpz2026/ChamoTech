# ChamoTech — TechAndes REST API

Catalog and order management backend for the tech store **TechAndes**.

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
