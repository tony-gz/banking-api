# API-Bank

REST API para gestión bancaria con autenticación JWT.

## Tecnologías

- **Java 21** con Spring Boot 3.5
- **Spring Security** (JWT)
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**

## Instalación

```bash
# Clonar el proyecto
git clone <repo-url>
cd API-Bank

# Configurar variables de entorno (o editar src/main/resources/application.yaml)
export DB_USERNAME=postgres
export DB_PASSWORD=tu_password
export JWT_SECRET=tu_secret_key
```

## Ejecución

```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`

## Endpoints

### Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/register` | Registrar usuario |
| POST | `/api/auth/login` | Iniciar sesión |

### Cuentas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/accounts` | Crear cuenta |
| GET | `/api/accounts/{accountNumber}/balance` | Consultar saldo |

### Transacciones

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/transactions/transfer` | Transferencia entre cuentas |

## Ejemplos de uso

### Registro de usuario

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "password": "password123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@example.com",
    "password": "password123"
  }'
```

### Crear cuenta (requiere JWT)

```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "accountNumber": "ACC001",
    "initialBalance": 1000.00
  }'
```

### Transferencia

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "sourceAccountNumber": "ACC001",
    "destinationAccountNumber": "ACC002",
    "amount": 100.00
  }'
```

## Docker

```bash
docker-compose up -d
```

## Licencia

MIT
