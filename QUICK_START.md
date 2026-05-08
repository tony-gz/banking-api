# 🎯 QUICK START GUIDE

## En 5 Minutos

### 1️⃣ Iniciar la Aplicación
```bash
cd /home/tony/JAVAProjects/Proyectos_SpringBoot/API-Bank
./mvnw spring-boot:run
```
⏱️ Esperar ~30 segundos a que inicie

### 2️⃣ Abrir Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### 3️⃣ Registrar un Usuario
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@mail.com","password":"pass123"}'
```

Copiar el **token** de la respuesta

### 4️⃣ En Swagger UI: Autorizar
1. Click botón "Authorize" (arriba a la derecha)
2. Pegar: `Bearer <TOKEN>`
3. Click "Authorize"

### 5️⃣ Probar Transferencia
```bash
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "sourceAccountNumber":"ACC001",
    "destinationAccountNumber":"ACC002",
    "amount":100.00
  }'
```

✅ **¡Listo!**

---

## 📚 Documentación Completa

| Documento | Descripción |
|-----------|-------------|
| `TESTING_GUIDE.md` | Guía detallada de pruebas |
| `CURL_EXAMPLES.md` | Todos los comandos curl |
| `RESUMEN.md` | Resumen visual de cambios |
| `README_CAMBIOS.md` | Documentación completa |

---

## 🔍 Ver Logs

```bash
tail -f app.log
```

Verás:
```
INFO  - Starting transfer from ACC001 to ACC002 amount 100.00
INFO  - Validations passed for transfer
INFO  - Transfer successful from ACC001 to ACC002 amount 100.00
```

---

## 📊 Swagger UI Endpoints

### 🔓 Públicos (sin JWT)
- `POST /api/auth/register` - Registrarse
- `POST /api/auth/login` - Login
- `GET /swagger-ui.html` - Documentación

### 🔒 Protegidos (requieren JWT)
- `POST /api/transactions/transfer` - Transferencia

---

## ⚠️ Errores Comunes

| Problema | Solución |
|----------|----------|
| "Connection refused" | Verificar que app está corriendo |
| Token inválido en Swagger | Usar formato: `Bearer <TOKEN>` |
| Cuenta no encontrada | Las cuentas se crean automáticamente |
| CORS error | Ya está configurado |

---

## 💡 Lo Nuevo

✅ **Swagger UI** - Documentación interactiva  
✅ **Logging** - app.log con todas las operaciones  
✅ **DTOs Response** - Respuestas JSON consistentes  
✅ **Validaciones** - Multi-capa (Bean + Service)  
✅ **Excepciones** - Personalizadas con mensajes claros  
✅ **Seguridad** - JWT integrado en OpenAPI  

---

## 🚀 Comando Todo en Uno

```bash
# Terminal 1
cd /home/tony/JAVAProjects/Proyectos_SpringBoot/API-Bank
./mvnw spring-boot:run

# Terminal 2 - Esperar a que inicie
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"T","email":"t@t.com","password":"p"}' | \
  grep -o '"token":"[^"]*' | cut -d'"' -f4)

# Transferencia
curl -X POST http://localhost:8080/api/transactions/transfer \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"sourceAccountNumber":"A1","destinationAccountNumber":"A2","amount":50.00}'

# Ver logs
tail -20 app.log
```

---

## ✨ Estado

✅ Swagger/OpenAPI completamente integrado  
✅ Logging con @Slf4j funcional  
✅ DTOs de respuesta implementados  
✅ Validaciones robustas  
✅ Excepciones personalizadas  
✅ Seguridad JWT integrada  
✅ Tests automatizados (test-api.sh)  
✅ Documentación completa  

**🎉 Todo listo para usar y presentar en entrevistas**

