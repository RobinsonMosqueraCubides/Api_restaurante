# API Restaurante

API REST para la gestion de un restaurante, desarrollada con **Spring Boot 3.2**, **PostgreSQL** y **Arquitectura Hexagonal**.

## Stack Tecnologico

- **Java 21** (OpenJDK Temurin)
- **Spring Boot 3.2.0**
- **Spring Data JPA / Hibernate**
- **Spring Security + JWT**
- **PostgreSQL 18**
- **Flyway** (migraciones de BD)
- **SpringDoc OpenAPI** (documentacion Swagger)
- **BCrypt** (encriptacion de passwords)
- **Maven 3.9.6**

## Arquitectura Hexagonal

```
src/main/java/com/restaurante/
├── domain/                    ← Dominio puro (sin dependencias de frameworks)
│   ├── model/                 ← Entidades de negocio
│   ├── port/
│   │   ├── input/             ← Casos de uso (interfaces)
│   │   └── output/            ← Repositorios (interfaces)
│   └── service/               ← Implementacion de casos de uso
├── infrastructure/
│   └── adapter/
│       ├── inbound/rest/      ← Controllers REST + DTOs
│       └── outbound/persistence/
│           ├── entity/        ← Entidades JPA (@Entity)
│           ├── jpa/           ← Repositorios Spring Data
│           └── adapter/       ← Adaptadores (Port -> JPA)
└── config/                    ← Configuracion Spring (Security, JWT, Beans)
```

## Prerequisitos

- Java 21+
- Maven 3.9+
- PostgreSQL 15+

## Instalacion

### 1. Clonar el repositorio

```bash
git clone git@github.com:RobinsonMosqueraCubides/Api_restaurante.git
cd Api_restaurante
```

### 2. Configurar base de datos

```sql
-- Conectar como postgres y crear usuario + base de datos
CREATE USER robin WITH PASSWORD '1095827105';
CREATE DATABASE api_restaurante OWNER robin;
\c api_restaurante
CREATE SCHEMA restaurante AUTHORIZATION robin;
GRANT ALL ON SCHEMA public TO robin;
```

### 3. Crear archivo `.env`

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=api_restaurante
DB_USERNAME=robin
DB_PASSWORD=1095827105
JWT_SECRET=c3VwZXItc2VjcmV0LWtleS1mb3ItYXBpLXJlc3RhdXJhbnRlLTIwMjY=
JWT_EXPIRATION=86400000
SERVER_PORT=8080
```

### 4. Compilar y ejecutar

```bash
export JAVA_HOME=~/.local/java
export PATH=$JAVA_HOME/bin:$PATH

# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run
```

La API estara disponible en `http://localhost:8080`

## Endpoints

### Autenticacion

| Metodo | Endpoint | Descripcion | Body |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Registrar usuario | `RegisterRequest` |
| POST | `/api/auth/login` | Iniciar sesion | `LoginRequest` |
| GET | `/api/auth/usuarios` | Listar usuarios | - |

### Categorias

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| POST | `/api/categorias` | Crear categoria |
| GET | `/api/categorias` | Listar categorias |
| GET | `/api/categorias/{id}` | Buscar categoria |
| PUT | `/api/categorias/{id}` | Actualizar categoria |
| DELETE | `/api/categorias/{id}` | Eliminar categoria |

### Platos

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| POST | `/api/platos` | Crear plato |
| GET | `/api/platos` | Listar platos |
| GET | `/api/platos?categoriaId=1` | Listar por categoria |
| GET | `/api/platos/{id}` | Buscar plato |
| PUT | `/api/platos/{id}` | Actualizar plato |
| PATCH | `/api/platos/{id}/disponibilidad?disponible=true` | Cambiar disponibilidad |
| DELETE | `/api/platos/{id}` | Eliminar plato |

### Mesas

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| POST | `/api/mesas` | Crear mesa |
| GET | `/api/mesas` | Listar mesas |
| GET | `/api/mesas/libres` | Listar mesas libres |
| GET | `/api/mesas/{id}` | Buscar mesa |
| PATCH | `/api/mesas/{id}/ocupar` | Ocupar mesa |
| PATCH | `/api/mesas/{id}/liberar` | Liberar mesa |
| DELETE | `/api/mesas/{id}` | Eliminar mesa |

### Pedidos

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| POST | `/api/pedidos` | Crear pedido |
| GET | `/api/pedidos/activos` | Listar pedidos activos |
| GET | `/api/pedidos/{id}` | Buscar pedido |
| POST | `/api/pedidos/{id}/platos` | Agregar plato al pedido |
| PATCH | `/api/pedidos/{id}/preparacion` | Marcar en preparacion |
| PATCH | `/api/pedidos/{id}/entregar` | Marcar entregado |
| POST | `/api/pedidos/{id}/cancelar` | Cancelar pedido |

### Clientes

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| POST | `/api/clientes` | Crear cliente |
| GET | `/api/clientes` | Listar clientes |
| GET | `/api/clientes/{id}` | Buscar cliente |

### Facturas

| Metodo | Endpoint | Descripcion |
|--------|----------|-------------|
| GET | `/api/facturas` | Listar facturas |
| GET | `/api/facturas/{id}` | Buscar factura |
| GET | `/api/facturas/pedido/{pedidoId}` | Buscar factura por pedido |
| POST | `/api/facturas/pedido/{pedidoId}/generar?metodoPago=efectivo` | Generar factura |

## Documentacion Swagger

Disponible en: `http://localhost:8080/swagger-ui.html`

## Ejemplos con curl

### Registrar usuario

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Admin",
    "rol": "admin",
    "usuario": "admin",
    "contrasena": "123456"
  }'
```

### Login (obtener token JWT)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usuario": "admin",
    "contrasena": "123456"
  }'
```

### Usar token en requests protegidos

```bash
TOKEN="eyJhbGciOiJIUzI1NiJ9..."

# Crear categoria
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nombre": "Entradas",
    "descripcion": "Platos para compartir"
  }'

# Crear plato
curl -X POST http://localhost:8080/api/platos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "categoriaId": 1,
    "nombre": "Empanadas",
    "descripcion": "Empanadas de carne",
    "precio": 15000
  }'

# Crear mesa
curl -X POST http://localhost:8080/api/mesas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "numero": 1,
    "capacidad": 4,
    "ubicacion": "Terraza"
  }'

# Crear pedido
curl -X POST http://localhost:8080/api/pedidos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "mesaId": 1,
    "clienteId": null,
    "usuarioId": 1
  }'

# Agregar plato al pedido
curl -X POST http://localhost:8080/api/pedidos/1/platos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "platoId": 1,
    "cantidad": 2
  }'

# Generar factura
curl -X POST "http://localhost:8080/api/facturas/pedido/1/generar?metodoPago=efectivo" \
  -H "Authorization: Bearer $TOKEN"
```

## Variables de Entorno

| Variable | Default | Descripcion |
|----------|---------|-------------|
| `DB_HOST` | localhost | Host de PostgreSQL |
| `DB_PORT` | 5432 | Puerto de PostgreSQL |
| `DB_NAME` | api_restaurante | Nombre de la base de datos |
| `DB_USERNAME` | robin | Usuario de PostgreSQL |
| `DB_PASSWORD` | 1095827105 | Contrasena de PostgreSQL |
| `JWT_SECRET` | (ver .env) | Secreto para firmar tokens JWT |
| `JWT_EXPIRATION` | 86400000 | Expiracion del token en ms (24h) |
| `SERVER_PORT` | 8080 | Puerto del servidor |

## Tablas de Base de Datos

| Tabla | Descripcion |
|-------|-------------|
| `categorias` | Categorias de platos |
| `platos` | Platos del menu |
| `mesas` | Mesas del restaurante |
| `clientes` | Clientes registrados |
| `usuarios` | Usuarios del sistema (login) |
| `pedidos` | Pedidos realizados |
| `detalle_pedido` | Detalle de platos por pedido |
| `facturas` | Facturas generadas |

## Licencia

Proyecto privado - Robinson Mosquera Cubides
