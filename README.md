# 🏦 API-Bank

Secure banking REST API built with Spring Boot, JWT authentication and PostgreSQL.

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green?style=for-the-badge)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge)
![JWT](https://img.shields.io/badge/Auth-JWT-red?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)


## Features

- JWT Authentication & Authorization
- Role-based Security
- Money Transfers
- Account Management
- Transaction History
- Swagger/OpenAPI Documentation
- Unit & Service Testing
- Global Exception Handling
- PostgreSQL Integration
## Requisitos previos
- **Java 21** o superior
- **Maven 3.8+**
- **PostgreSQL 12+**
- **Git**
## Tecnologías
- **Java 21** con Spring Boot 3.5
- **Spring Security** (JWT)
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **JUnit 5** y **Mockito** (testing)
- **Swagger/OpenAPI** (documentación)
## Instalación
### 1. Clonar el proyecto
```bash
git clone <repo-url>
cd API-Bank
```
### 2. Configurar base de datos
Crear base de datos PostgreSQL:
```sql
CREATE DATABASE bankdb;
CREATE USER bankuser WITH PASSWORD 'tu_password';
ALTER ROLE bankuser SET client_encoding TO 'utf8';
GRANT ALL PRIVILEGES ON DATABASE bankdb TO bankuser;
```
### 3. Crear archivo de entorno
```bash
cp .env.example .env
```
### 4. Configurar variables de entorno
Editar `.env` con tus valores:
```
DB_URL=jdbc:postgresql://localhost:5432/bankdb
DB_USERNAME=bankuser
DB_PASSWORD=tu_password
JWT_SECRET=<genera uno con: openssl rand -base64 64>
JWT_EXPIRATION=86400000
```
Genera un secreto JWT seguro:
```bash
openssl rand -base64 64
```
## Ejecución
### Desde terminal
```bash
./mvnw spring-boot:run
```
### Desde IntelliJ IDEA
1. Click derecho en el proyecto → `Run 'API-Bank [spring-boot:run]'`
2. O usar el botón `Run` en la clase principal
La API estará disponible en `http://localhost:8080`
## Testing
### Ejecutar todas las pruebas
```bash
./mvnw test
```
### Ejecutar pruebas de una clase específica
```bash
./mvnw test -Dtest=UserControllerTest
```
## Documentación con Swagger
Acceder a la interfaz de Swagger UI:
```
http://localhost:8080/swagger-ui.html
```
Documentación OpenAPI JSON:
```
http://localhost:8080/v3/api-docs
```
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
**Respuesta exitosa (201):**
```json
{
  "id": 1,
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "createdAt": "2024-01-15T10:30:00"
}
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
**Respuesta exitosa (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```
### Crear cuenta (requiere JWT)
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
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
**Respuesta exitosa (200):**
```json
{
  "message": "Transfer successful",
  "sourceAccount": "ACC001",
  "destinationAccount": "ACC002",
  "amount": 100.00,
  "timestamp": "2024-01-15T10:35:00"
}
```
## Pruebas con Postman
### 1. Configurar variable de entorno
En Postman, crear un environment con:
```json
{
  "base_url": "http://localhost:8080",
  "token": ""
}
```
### 2. Flujo de prueba recomendado
1. **Registrar usuario** → Guardar ID
2. **Login** → Copiar token a variable `token`
3. **Crear cuenta** → Usar el ID del usuario
4. **Consultar saldo** → Verificar inicialización
5. **Transferencia** → Probar entre cuentas
## Estructura del proyecto
```
src/main/java/com/tony/bankapi/
├── controller/        # Controladores REST
├── service/          # Lógica de negocio
├── repository/       # Acceso a datos
├── dto/              # Data Transfer Objects
├── entity/           # Entidades JPA
├── config/           # Configuración (CORS, JWT, Swagger)
└── exception/        # Manejo centralizado de errores
src/test/java/com/tony/bankapi/
├── controller/       # Tests de controladores
└── service/          # Tests de servicios
```
## Solución de problemas
### Error: `FATAL: role "postgres" does not exist`
Asegurate que PostgreSQL está corriendo y el usuario existe:
```bash
psql -U postgres -l
```
### Error: `JWT secret not configured`
Verifica que la variable `JWT_SECRET` está definida en `.env`.
### Error: Puerto 8080 ocupado
Si el puerto 8080 está ocupado, usa otro:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```
### Error de conexión a BD
Verifica credenciales en `application.yaml`:
```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/bankdb}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD}
```
## Docker
```bash
docker-compose up -d
```
## Licencia
MIT
