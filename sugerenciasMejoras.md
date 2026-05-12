# Sugerencias de Mejoras - API Restaurante

**Fecha de revisión:** 2026-05-11
**Revisó:** Mimo (MiMo-V2.5-Pro)
**Proyecto:** Api_restaurante (Spring Boot 3.2 + PostgreSQL + Hexagonal Architecture)

---

## Resumen Ejecutivo

| Severidad | Cantidad | Estado |
|-----------|----------|--------|
| CRITICO   | 3        | Corregido |
| ALTO      | 3        | Corregido |
| MEDIO     | 5        | Corregido |
| BAJO      | 3        | Pendiente |
| **Total** | **17**   | |

---

## CRITICO

### 1. Mapeo JPA roto en PedidoEntity / DetallePedidoEntity

**Archivo:** `infrastructure/adapter/outbound/persistence/entity/PedidoEntity.java:34`
**Problema:** `@OneToMany(mappedBy = "pedidoId")` apunta a un campo `Long`, no a una relacion JPA. Hibernate lanzaria `MappingException` al iniciar.

**Solucion:** Se elimino el `@OneToMany` y se usa `@Query` en el repositorio para cargar detalles por `pedidoId`. Se mapea manualmente en el adapter.

---

### 2. SecurityConfig sin mecanismo de autenticacion

**Archivo:** `infrastructure/config/SecurityConfig.java`
**Problema:** `.anyRequest().authenticated()` pero no hay JWT filter, no hay `UserDetailsService`. Todas las peticiones a `/api/*` devuelven **403 Forbidden**.

**Solucion:** Se implemento:
- `infrastructure/config/JwtService.java` — Generacion y validacion de tokens JWT
- `infrastructure/config/JwtAuthenticationFilter.java` — Filtro que intercepta requests y valida el token
- Se registro el filtro en `SecurityConfig` con `sessionManagement(STATELESS)`

---

### 3. Passwords en texto plano

**Archivo:** `domain/service/AutenticacionService.java:42-48`
**Problema:** `verificarContrasena()` hace `raw.equals(hash)` y `hashContrasena()` retorna la contrasena sin encriptar.

**Solucion:** Se inyecto `BCryptPasswordEncoder` desde `BeanConfiguration` y se usa `encoder.matches()` / `encoder.encode()`.

---

## ALTO

### 4. ClienteController inyecta Repository directamente

**Archivo:** `infrastructure/adapter/inbound/rest/ClienteController.java`
**Problema:** Inyecta `ClienteRepository` (output port) directamente, saltandose el caso de uso. Viola la Arquitectura Hexagonal.

**Solucion:** Se creo `GestionClientesUseCase` (input port) y `GestionClientesService` (implementacion). El controller ahora inyecta el caso de uso.

---

### 5. PedidoController usa Map<String, Object>

**Archivo:** `infrastructure/adapter/inbound/rest/PedidoController.java:30-35`
**Problema:** `agregarPlato()` acepta `Map<String, Object>` sin validacion, sin documentacion OpenAPI, riesgo de NPE.

**Solucion:** Se creo el DTO `AgregarPlatoRequest` con campos `platoId` (Long) y `cantidad` (int).

---

### 6. Sin manejador global de excepciones

**Problema:** Sin `@ControllerAdvice`, los errores devuelven 500 generico con stack trace.

**Solucion:** Se creo `infrastructure/config/GlobalExceptionHandler.java` con:
- `IllegalArgumentException` → 400 Bad Request
- `IllegalStateException` → 409 Conflict
- `Exception` generica → 500 Internal Server Error

---

## MEDIO

### 7. Falta @Transactional en servicios con multiples writes

**Archivos:** `FacturacionService`, `TomarPedidoService`, `GestionMenuService`
**Problema:** Si falla un paso a mitad de transaccion, la BD queda en estado inconsistente.

**Solucion:** Se agrego `@Transactional` a los metodos que realizan multiples operaciones de escritura.

---

### 8. Sin anotaciones de Bean Validation en DTOs

**Archivos:** Todos los DTOs (`CrearPedidoRequest`, `CrearCategoriaRequest`, etc.)
**Problema:** Se aceptan entradas vacias o invalidas sin validacion.

**Solucion:** Se agregaron anotaciones `@NotBlank`, `@NotNull`, `@Min`, `@Email` a todos los DTOs.

---

### 9. .gitignore es de Node.js, no de Java/Maven

**Archivo:** `.gitignore`
**Problema:** Contiene `node_modules/`, `dist/`, `build/`. Falta `target/`, `.idea/`, `*.iml`.

**Solucion:** Se reescribio el `.gitignore` con entradas correctas para Java/Maven.

---

### 10. Archivo schema.sql duplicado en raiz

**Archivo:** `schema.sql` (raiz del proyecto)
**Problema:** Copia identica de `V1__init.sql`. Puede causar confusion o errores de duplicacion.

**Solucion:** Se elimino el archivo duplicado.

---

### 11. AuthController retorna Usuario con hash de contrasena

**Archivo:** `infrastructure/adapter/inbound/rest/AuthController.java`
**Problema:** El endpoint `/api/auth/login` y `/api/auth/usuarios` devuelven el objeto `Usuario` completo, incluyendo `contrasenaHash`.

**Solucion:** Se creo `UsuarioResponse` DTO que excluye el campo de contrasena.

---

## BAJO

### 12. Estados hardcodeados como strings

**Archivos:** `Pedido.java`, `Mesa.java`, `Factura.java`
**Problema:** Strings como `"activo"`, `"preparacion"`, `"libre"`, `"ocupada"` sin tipos seguros.

**Pendiente:** Crear enums `EstadoPedido`, `EstadoMesa`, `EstadoFactura`.

---

### 13. Sin tests unitarios ni de integracion

**Problema:** No existe ningun archivo de test a pesar de tener `spring-boot-starter-test` como dependencia.

**Pendiente:** Crear tests para servicios de dominio y tests de integracion para endpoints REST.

---

### 14. Controllers retornan objetos de dominio directamente

**Problema:** `ResponseEntity<Cliente>`, `ResponseEntity<Pedido>` etc. Fuga del modelo interno a la API.

**Pendiente:** Crear Response DTOs para cada endpoint (ClienteResponse, PedidoResponse, etc.).

---

## Archivos Modificados/Creados

| Archivo | Accion |
|---------|--------|
| `sugerenciasMejoras.md` | CREADO |
| `infrastructure/config/JwtService.java` | CREADO |
| `infrastructure/config/JwtAuthenticationFilter.java` | CREADO |
| `infrastructure/config/SecurityConfig.java` | MODIFICADO |
| `infrastructure/config/BeanConfiguration.java` | MODIFICADO |
| `infrastructure/config/GlobalExceptionHandler.java` | CREADO |
| `domain/service/AutenticacionService.java` | MODIFICADO |
| `domain/port/input/GestionClientesUseCase.java` | CREADO |
| `domain/service/GestionClientesService.java` | CREADO |
| `infrastructure/adapter/inbound/rest/ClienteController.java` | MODIFICADO |
| `infrastructure/adapter/inbound/rest/PedidoController.java` | MODIFICADO |
| `infrastructure/adapter/inbound/rest/AgregarPlatoRequest.java` | CREADO |
| `infrastructure/adapter/inbound/rest/UsuarioResponse.java` | CREADO |
| `infrastructure/adapter/inbound/rest/AuthController.java` | MODIFICADO |
| `infrastructure/adapter/outbound/persistence/entity/PedidoEntity.java` | MODIFICADO |
| `infrastructure/adapter/outbound/persistence/jpa/SpringDataPedidoRepository.java` | MODIFICADO |
| `infrastructure/adapter/outbound/persistence/adapter/JpaPedidoRepositoryAdapter.java` | MODIFICADO |
| `domain/port/input/GestionMenuUseCase.java` | MODIFICADO (agregarPlato) |
| `domain/service/GestionMenuService.java` | MODIFICADO (@Transactional) |
| `domain/service/TomarPedidoService.java` | MODIFICADO (@Transactional) |
| `domain/service/FacturacionService.java` | MODIFICADO (@Transactional) |
| `infrastructure/adapter/inbound/rest/CrearCategoriaRequest.java` | MODIFICADO (validations) |
| `infrastructure/adapter/inbound/rest/CrearPlatoRequest.java` | MODIFICADO (validations) |
| `infrastructure/adapter/inbound/rest/CrearMesaRequest.java` | MODIFICADO (validations) |
| `infrastructure/adapter/inbound/rest/CrearPedidoRequest.java` | MODIFICADO (validations) |
| `infrastructure/adapter/inbound/rest/LoginRequest.java` | MODIFICADO (validations) |
| `infrastructure/adapter/inbound/rest/RegisterRequest.java` | MODIFICADO (validations) |
| `infrastructure/adapter/inbound/rest/ClienteRequest.java` | MODIFICADO (validations) |
| `.gitignore` | MODIFICADO |
| `schema.sql` | ELIMINADO |
