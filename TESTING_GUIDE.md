# API Bank - Testing Guide

## Cambios Realizados

### 1. **Agregado Swagger/OpenAPI**
- ✅ Dependencia `springdoc-openapi-starter-webmvc-ui` en `pom.xml`
- ✅ Clase `OpenApiConfig.java` creada en `config/`
- ✅ Anotaciones `@Tag` y `@Operation` en `TransactionController`
- ✅ Anotaciones `@Schema` en `TransferRequest`

### 2. **Sistema de Logging con @Slf4j**
- ✅ `@Slf4j` agregado a `TransactionServiceImpl`
- ✅ Logs de:
  - Inicio de transferencia
  - Validaciones (montos inválidos, misma cuenta)
  - Errores (cuentas no encontradas, fondos insuficientes)
  - Éxito de la operación
- ✅ `@Slf4j` agregado a `GlobalExceptionHandler`
- ✅ Logs en todos los manejadores de excepciones

### 3. **Configuración de Logging**
- ✅ `application.yaml` configurado con:
  - `logging.level.com.tony.bankapi: INFO`
  - `logging.file.name: app.log`

### 4. **DTOs de Respuesta**
- ✅ `TransferResponse.java` creado con los campos:
  - `message`: Mensaje de éxito
  - `sourceAccount`: Número de cuenta origen
  - `destinationAccount`: Número de cuenta destino
  - `amount`: Monto transferido
  - `timestamp`: Fecha y hora

### 5. **Bean Validation en TransferRequest**
- ✅ `@NotBlank` en números de cuenta
- ✅ `@NotNull` y `@DecimalMin("0.01")` en monto
- ✅ Anotaciones `@Schema` para documentación Swagger

### 6. **Validaciones en el Servicio**
- ✅ Validación de monto > 0
- ✅ Validación para no transferir a la misma cuenta
- ✅ Excepciones personalizadas:
  - `AccountNotFoundException`
  - `InsufficientFundsException`
  - `BadRequestException`

### 7. **Seguridad y Swagger**
- ✅ `SecurityConfig.java` actualizado para permitir acceso a:
  - `/swagger-ui/**`
  - `/v3/api-docs/**`
  - `/api/auth/**`

---

## Instrucciones para Probar

### Paso 1: Iniciar la Aplicación
```bash
cd /home/tony/JAVAProjects/Proyectos_SpringBoot/API-Bank
./mvnw spring-boot:run
```

### Paso 2: Acceder a Swagger UI
Abrir en el navegador:
```
http://localhost:8080/swagger-ui.html
```

O también:
```
http://localhost:8080/swagger-ui/index.html
```

### Paso 3: Registrar un Usuario
Hacer una petición POST a `/api/auth/register`:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "password": "password123"
  }'
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "juan@example.com",
  "role": "USER"
}
```

### Paso 4: Login (Obtener JWT Token)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@example.com",
    "password": "password123"
  }'
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "juan@example.com",
  "role": "USER"
}
```

### Paso 5: Usar JWT en Swagger UI
1. Hacer clic en el botón "Authorize" en Swagger UI
2. Pegar el token obtenido del login: `Bearer eyJhbGciOiJIUzI1NiJ9...`
3. Hacer clic en "Authorize"

### Paso 6: Probar el Endpoint de Transferencia
Hacer una petición POST a `/api/transactions/transfer` con JWT:
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..." \
  -d '{
    "sourceAccountNumber": "123456",
    "destinationAccountNumber": "654321",
    "amount": 500.00
  }'
```

**Respuesta esperada:**
```json
{
  "message": "Transfer successful",
  "sourceAccount": "123456",
  "destinationAccount": "654321",
  "amount": 500.00,
  "timestamp": "2026-04-15T13:45:30"
}
```

### Paso 7: Ver Logs
Monitorear el archivo de logs:
```bash
tail -f app.log
```

O en la consola de la aplicación verás algo como:
```
2026-04-15 13:45:30 INFO com.tony.bankapi.service.impl.TransactionServiceImpl - Starting transfer from 123456 to 654321 amount 500.00
2026-04-15 13:45:30 INFO com.tony.bankapi.service.impl.TransactionServiceImpl - Validations passed for transfer
2026-04-15 13:45:30 INFO com.tony.bankapi.service.impl.TransactionServiceImpl - Transfer successful from 123456 to 654321 amount 500.00
```

---

## Pruebas de Validación

### Test 1: Transferencia con monto inválido (0)
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "sourceAccountNumber": "123456",
    "destinationAccountNumber": "654321",
    "amount": 0
  }'
```
**Resultado esperado:** Error 400 - "Amount must be greater than zero"

### Test 2: Transferencia a la misma cuenta
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "sourceAccountNumber": "123456",
    "destinationAccountNumber": "123456",
    "amount": 500.00
  }'
```
**Resultado esperado:** Error 400 - "Cannot transfer to the same account"

### Test 3: Cuenta no encontrada
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "sourceAccountNumber": "999999",
    "destinationAccountNumber": "654321",
    "amount": 500.00
  }'
```
**Resultado esperado:** Error 404 - "Source account not found"

### Test 4: Sin JWT Token
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -d '{
    "sourceAccountNumber": "123456",
    "destinationAccountNumber": "654321",
    "amount": 500.00
  }'
```
**Resultado esperado:** Error 403 - Acceso denegado

---

## Documentación en Swagger UI

Una vez en http://localhost:8080/swagger-ui.html, podrás ver:

### Tag: Transactions
- **POST /api/transactions/transfer**
  - Summary: "Transfer money between accounts"
  - Description: "Transfers a specified amount from source account to destination account"
  - Request Body:
    - `sourceAccountNumber`: string - "Account number of the source account"
    - `destinationAccountNumber`: string - "Account number of the destination account"
    - `amount`: number - "Amount to transfer"
  - Response 200: TransferResponse

---

## Archivos Modificados/Creados

| Archivo | Acción | Descripción |
|---------|--------|-------------|
| `pom.xml` | Modificado | Agregada dependencia Swagger |
| `OpenApiConfig.java` | Creado | Configuración OpenAPI/Swagger |
| `TransactionController.java` | Modificado | Agregadas anotaciones Swagger |
| `TransactionService.java` | Modificado | Retorna TransferResponse |
| `TransactionServiceImpl.java` | Modificado | Agregados logs, retorna TransferResponse |
| `TransferRequest.java` | Modificado | Agregadas anotaciones @Schema |
| `TransferResponse.java` | Creado | DTO de respuesta |
| `GlobalExceptionHandler.java` | Modificado | Agregados logs en excepciones |
| `SecurityConfig.java` | Modificado | Permitir acceso a Swagger |
| `application.yaml` | Modificado | Configuración de logging |

---

## Próximos Pasos (Opcional)

1. Agregar anotaciones Swagger a otros controllers (`UserController`, `AccountController`, `AdminController`)
2. Implementar `@SecurityScheme` para JWT en OpenAPI
3. Crear tests unitarios para validar las transferencias
4. Implementar rate limiting
5. Agregar auditoría de cambios en base de datos


