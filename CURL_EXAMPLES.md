# Ejemplos de CURL para Pruebas

## 1. Registrar un Usuario

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "password": "password123"
  }'
```

**Respuesta esperada (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuQGV4YW1wbGUuY29tIiwiaWF0IjoxNzEzMTk0NzMwLCJleHAiOjE3MTMyODExMzB9.xxx",
  "email": "juan@example.com",
  "role": "USER"
}
```

---

## 2. Login (Obtener Token)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@example.com",
    "password": "password123"
  }'
```

**Respuesta esperada (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuQGV4YW1wbGUuY29tIiwiaWF0IjoxNzEzMTk0NzMwLCJleHAiOjE3MTMyODExMzB9.xxx",
  "email": "juan@example.com",
  "role": "USER"
}
```

---

## 3. Transferencia Exitosa

Guardar el token en una variable:
```bash
TOKEN="eyJhbGciOiJIUzI1NiJ9..."
```

Realizar una transferencia:
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sourceAccountNumber": "ACC001",
    "destinationAccountNumber": "ACC002",
    "amount": 500.00
  }'
```

**Respuesta esperada (200 OK):**
```json
{
  "message": "Transfer successful",
  "sourceAccount": "ACC001",
  "destinationAccount": "ACC002",
  "amount": 500.00,
  "timestamp": "2026-04-15T13:45:30.123456"
}
```

---

## 4. Validación: Monto Cero

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sourceAccountNumber": "ACC001",
    "destinationAccountNumber": "ACC002",
    "amount": 0
  }'
```

**Respuesta esperada (400 BAD REQUEST):**
```json
{
  "status": 400,
  "message": "Amount must be greater than zero",
  "path": "/api/transactions/transfer",
  "timestamp": "2026-04-15T13:46:00.000000"
}
```

---

## 5. Validación: Monto Negativo

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sourceAccountNumber": "ACC001",
    "destinationAccountNumber": "ACC002",
    "amount": -100.00
  }'
```

**Respuesta esperada (400 BAD REQUEST):**
```json
{
  "status": 400,
  "message": "Amount must be greater than zero",
  "path": "/api/transactions/transfer",
  "timestamp": "2026-04-15T13:46:30.000000"
}
```

---

## 6. Validación: Misma Cuenta

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sourceAccountNumber": "ACC001",
    "destinationAccountNumber": "ACC001",
    "amount": 100.00
  }'
```

**Respuesta esperada (400 BAD REQUEST):**
```json
{
  "status": 400,
  "message": "Cannot transfer to the same account",
  "path": "/api/transactions/transfer",
  "timestamp": "2026-04-15T13:47:00.000000"
}
```

---

## 7. Validación: Cuenta No Encontrada

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sourceAccountNumber": "NONEXISTENT",
    "destinationAccountNumber": "ACC002",
    "amount": 100.00
  }'
```

**Respuesta esperada (404 NOT FOUND):**
```json
{
  "status": 404,
  "message": "Source account not found",
  "path": "/api/transactions/transfer",
  "timestamp": "2026-04-15T13:47:30.000000"
}
```

---

## 8. Validación: Sin JWT Token

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "sourceAccountNumber": "ACC001",
    "destinationAccountNumber": "ACC002",
    "amount": 100.00
  }'
```

**Respuesta esperada (403 FORBIDDEN):**
```json
{
  "status": 403,
  "message": "Forbidden",
  "path": "/api/transactions/transfer",
  "timestamp": "2026-04-15T13:48:00.000000"
}
```

---

## 9. Validación: JWT Token Inválido

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer invalid_token_123" \
  -d '{
    "sourceAccountNumber": "ACC001",
    "destinationAccountNumber": "ACC002",
    "amount": 100.00
  }'
```

**Respuesta esperada (403 FORBIDDEN):**
```json
{
  "status": 403,
  "message": "Forbidden",
  "path": "/api/transactions/transfer",
  "timestamp": "2026-04-15T13:48:30.000000"
}
```

---

## 10. Validación: Campo Requerido Faltante

```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sourceAccountNumber": "ACC001",
    "amount": 100.00
  }'
```

**Respuesta esperada (400 BAD REQUEST):**
```json
{
  "destinationAccountNumber": "must not be blank"
}
```

---

## 11. Ver OpenAPI Docs (JSON)

```bash
curl -s http://localhost:8080/v3/api-docs | jq .
```

---

## 12. Ver Swagger UI en Navegador

Abrir en el navegador:
```
http://localhost:8080/swagger-ui.html
```

O:
```
http://localhost:8080/swagger-ui/index.html
```

---

## Script Rápido para Guardar Token

```bash
#!/bin/bash

# Registrarse
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "testpass123"
  }')

# Extraer token
TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)

echo "Token: $TOKEN"

# Guardar en variable de entorno
export TOKEN=$TOKEN

# Usar en siguiente request
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sourceAccountNumber": "ACC001",
    "destinationAccountNumber": "ACC002",
    "amount": 250.00
  }'
```

---

## Variables de Entorno Útiles

```bash
# URL base
export BASE_URL="http://localhost:8080"

# Email y contraseña
export EMAIL="juan@example.com"
export PASSWORD="password123"

# Obtener token
export TOKEN=$(curl -s -X POST $BASE_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"$EMAIL\",\"password\":\"$PASSWORD\"}" | grep -o '"token":"[^"]*' | cut -d'"' -f4)

echo "Token obtenido: $TOKEN"
```

---

## Monitorear Logs

```bash
# En tiempo real
tail -f app.log

# Últimas 20 líneas
tail -20 app.log

# Filtrar por palabra clave
grep "Transfer" app.log

# Filtrar por nivel (ERROR, WARN, INFO)
grep "ERROR" app.log
```

