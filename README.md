# 🏦 Banking API

Secure banking REST API built with Spring Boot, JWT authentication and PostgreSQL.

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green?style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge)
![JWT](https://img.shields.io/badge/Auth-JWT-red?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

## 📋 Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Technologies](#technologies)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [API Documentation](#api-documentation)
- [Endpoints](#endpoints)
- [Usage Examples](#usage-examples)
- [Postman Testing](#postman-testing)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [Docker](#docker)
- [License](#license)

## ✨ Features

- 🔐 **JWT Authentication & Authorization** - Secure token-based authentication
- 👥 **Role-based Security** - Different access levels for users
- 💸 **Money Transfers** - Secure inter-account transfers
- 💼 **Account Management** - Create and manage bank accounts
- 📊 **Transaction History** - Track all transactions
- 📚 **Swagger/OpenAPI Documentation** - Interactive API documentation
- ✅ **Unit & Service Testing** - Comprehensive test coverage with JUnit 5 & Mockito
- 🚨 **Global Exception Handling** - Centralized error management
- 🗄️ **PostgreSQL Integration** - Reliable relational database

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.8+**
- **PostgreSQL 12+**
- **Git**

## 🛠️ Technologies

- **Java 21** with Spring Boot 3.5
- **Spring Security** (JWT)
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **JUnit 5** and **Mockito** (testing)
- **Swagger/OpenAPI** (API documentation)

## 🚀 Installation

### 1. Clone the project

```bash
git clone <repo-url>
cd API-Bank
```

### 2. Configure database

Create PostgreSQL database:

```sql
CREATE DATABASE bankdb;
CREATE USER bankuser WITH PASSWORD 'your_password';
ALTER ROLE bankuser SET client_encoding TO 'utf8';
GRANT ALL PRIVILEGES ON DATABASE bankdb TO bankuser;
```

### 3. Create environment file

```bash
cp .env.example .env
```

### 4. Configure environment variables

Edit `.env` with your values:

```env
DB_URL=jdbc:postgresql://localhost:5432/bankdb
DB_USERNAME=bankuser
DB_PASSWORD=your_password
JWT_SECRET=<generate with: openssl rand -base64 64>
JWT_EXPIRATION=86400000
```

Generate a secure JWT secret (run once):

```bash
openssl rand -base64 64
```

## ▶️ Running the Application

### From terminal

```bash
./mvnw spring-boot:run
```

### From IntelliJ IDEA

1. Right-click on the project → `Run 'API-Bank [spring-boot:run]'`
2. Or use the `Run` button in the main class

The API will be available at `http://localhost:8080`

## 🧪 Testing

### Run all tests

```bash
./mvnw test
```

### Run specific test class

```bash
./mvnw test -Dtest=UserControllerTest
```

## 📚 API Documentation

Access the Swagger UI interface:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON documentation:

```
http://localhost:8080/v3/api-docs
```

## 🔌 Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | User login |

### Accounts

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/accounts` | Create a new account |
| GET | `/api/accounts/{accountNumber}/balance` | Get account balance |

### Transactions

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/transactions/transfer` | Transfer money between accounts |

## 📝 Usage Examples

### User Registration

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

**Successful Response (201):**
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "createdAt": "2024-01-15T10:30:00"
}
```

### User Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securePassword123"
  }'
```

**Successful Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

### Create Account (requires JWT)

```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "ACC001",
    "initialBalance": 1000.00
  }'
```

### Money Transfer

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

**Successful Response (200):**
```json
{
  "message": "Transfer successful",
  "sourceAccount": "ACC001",
  "destinationAccount": "ACC002",
  "amount": 100.00,
  "timestamp": "2024-01-15T10:35:00"
}
```

## 📮 Postman Testing

### 1. Configure Environment Variables

In Postman, create an environment with:

```json
{
  "base_url": "http://localhost:8080",
  "token": ""
}
```

### 2. Recommended Testing Flow

1. **Register User** → Save the user ID
2. **Login** → Copy token to `token` variable
3. **Create Account** → Use the user ID
4. **Check Balance** → Verify initialization
5. **Transfer Money** → Test between accounts

## 📁 Project Structure

```
src/main/java/com/tony/bankapi/
├── controller/        # REST Controllers
├── service/          # Business Logic
├── repository/       # Data Access
├── dto/              # Data Transfer Objects
├── entity/           # JPA Entities
├── config/           # Configuration (CORS, JWT, Swagger)
└── exception/        # Centralized Error Handling

src/test/java/com/tony/bankapi/
├── controller/       # Controller Tests
└── service/          # Service Tests
```

## 🔧 Troubleshooting

### Error: `FATAL: role "postgres" does not exist`

Ensure PostgreSQL is running and the user exists:

```bash
psql -U postgres -l
```

### Error: `JWT secret not configured`

Verify that the `JWT_SECRET` variable is set in `.env`.

### Error: Port 8080 already in use

If port 8080 is occupied, use another port:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Error: Database connection failed

Check credentials in `application.yaml`:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/bankdb}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

## 🐳 Docker

Run the entire application stack with Docker:

```bash
docker-compose up -d
```

Stop the services:

```bash
docker-compose down
```

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Made with ❤️ by Tony**

For questions or support, please open an issue in the repository.
