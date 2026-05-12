# Arquitectura Hexagonal — Api Restaurante

## Stack Tecnológico

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 17+ |
| Framework | Spring Boot 3.x |
| Build | Maven |
| BD | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Migraciones | Flyway |
| Documentación API | OpenAPI / SpringDoc |

---

## 1. ¿Qué es Arquitectura Hexagonal?

La **Arquitectura Hexagonal** (Ports & Adapters) organiza el código en capas concéntricas donde el **dominio** está en el centro, aislado de tecnologías externas (BD, API REST, colas, etc.).

```
                     ┌──────────────┐
                     │   Cliente    │
                     │  (HTTP/REST) │
                     └──────┬───────┘
                            │
                    ┌───────▼────────┐
                    │  Controller    │
                    │ (Adapter IN)   │
                    └───────┬────────┘
                            │
               ┌────────────▼────────────┐
               │     Application Layer    │
               │  (UseCases / Services)   │
               └────────────┬────────────┘
                            │
          ┌─────────────────▼─────────────────┐
          │       Domain Layer (Core)          │
          │  Entities + Ports (interfaces)     │
          └─────────────────┬─────────────────┘
                            │
               ┌────────────▼────────────┐
               │  Repository Adapter    │
               │ (Adapter OUT - JPA)    │
               └────────────┬────────────┘
                            │
                    ┌───────▼────────┐
                    │   PostgreSQL   │
                    └────────────────┘
```

**Principios:**
- **Dominio puro**: Sin anotaciones Spring, sin dependencias externas
- **Puertos (Ports)**: Interfaces que definen contratos (entrada y salida)
- **Adaptadores (Adapters)**: Implementaciones concretas de los puertos
- **Inversión de dependencias**: El dominio no sabe de Spring ni JPA

---

## 2. Estructura del Proyecto Maven

```
api-restaurante/
├── pom.xml
└── src/
    └── main/
        ├── java/com/restaurante/
        │   ├── ApiRestauranteApplication.java
        │   │
        │   ├── domain/                      ← CAPA DE DOMINIO (puro Java)
        │   │   ├── model/
        │   │   │   ├── Categoria.java
        │   │   │   ├── Plato.java
        │   │   │   ├── Mesa.java
        │   │   │   ├── Cliente.java
        │   │   │   ├── Usuario.java
        │   │   │   ├── Pedido.java
        │   │   │   ├── DetallePedido.java
        │   │   │   └── Factura.java
        │   │   ├── port/
        │   │   │   ├── input/              ← Puertos de entrada (casos de uso)
        │   │   │   │   ├── GestionMenuUseCase.java
        │   │   │   │   ├── TomarPedidoUseCase.java
        │   │   │   │   ├── GestionMesasUseCase.java
        │   │   │   │   ├── FacturacionUseCase.java
        │   │   │   │   └── AutenticacionUseCase.java
        │   │   │   └── output/             ← Puertos de salida (repositorios)
        │   │   │       ├── CategoriaRepository.java
        │   │   │       ├── PlatoRepository.java
        │   │   │       ├── MesaRepository.java
        │   │   │       ├── ClienteRepository.java
        │   │   │       ├── UsuarioRepository.java
        │   │   │       ├── PedidoRepository.java
        │   │   │       └── FacturaRepository.java
        │   │   └── service/                ← Implementación de casos de uso
        │   │       ├── GestionMenuService.java
        │   │       ├── TomarPedidoService.java
        │   │       ├── GestionMesasService.java
        │   │       ├── FacturacionService.java
        │   │       └── AutenticacionService.java
        │   │
        │   ├── application/                 ← CAPA DE APLICACIÓN
        │   │   ├── dto/
        │   │   │   ├── request/
        │   │   │   │   ├── CrearCategoriaRequest.java
        │   │   │   │   ├── CrearPlatoRequest.java
        │   │   │   │   ├── CrearPedidoRequest.java
        │   │   │   │   └── LoginRequest.java
        │   │   │   └── response/
        │   │   │       ├── CategoriaResponse.java
        │   │   │       ├── PlatoResponse.java
        │   │   │       ├── PedidoResponse.java
        │   │   │       └── FacturaResponse.java
        │   │   └── mapper/
        │   │       ├── CategoriaMapper.java
        │   │       ├── PlatoMapper.java
        │   │       └── PedidoMapper.java
        │   │
        │   └── infrastructure/              ← CAPA DE INFRAESTRUCTURA
        │       ├── adapter/
        │       │   ├── inbound/            ← Adaptadores de entrada (REST)
        │       │   │   └── rest/
        │       │   │       ├── CategoriaController.java
        │       │   │       ├── PlatoController.java
        │       │   │       ├── MesaController.java
        │       │   │       ├── PedidoController.java
        │       │   │       ├── FacturaController.java
        │       │   │       └── AuthController.java
        │       │   └── outbound/           ← Adaptadores de salida (JPA)
        │       │       └── persistence/
        │       │           ├── entity/     ← Entidades JPA (separadas del dominio)
        │       │           │   ├── CategoriaEntity.java
        │       │           │   ├── PlatoEntity.java
        │       │           │   ├── MesaEntity.java
        │       │           │   ├── ClienteEntity.java
        │       │           │   ├── UsuarioEntity.java
        │       │           │   ├── PedidoEntity.java
        │       │           │   ├── DetallePedidoEntity.java
        │       │           │   └── FacturaEntity.java
        │       │           ├── jpa/        ← Repositorios Spring Data JPA
        │       │           │   ├── SpringDataCategoriaRepository.java
        │       │           │   ├── SpringDataPlatoRepository.java
        │       │           │   ├── SpringDataPedidoRepository.java
        │       │           │   └── SpringDataFacturaRepository.java
        │       │           └── mapper/     ← Mappers Entity ↔ Domain
        │       │               ├── CategoriaEntityMapper.java
        │       │               ├── PlatoEntityMapper.java
        │       │               └── PedidoEntityMapper.java
        │       └── config/
        │           ├── SecurityConfig.java
        │           └── OpenApiConfig.java
        │
        └── resources/
            ├── application.yml
            ├── application-dev.yml
            └── db/migration/
                ├── V1__init.sql
                └── V2__datos_iniciales.sql
```

---

## 3. Capa de Dominio (Core)

### 3.1 Modelo de Dominio (POJOs puros, sin anotaciones)

Las entidades de dominio son clases Java planas sin ninguna dependencia de Spring ni JPA.

```java
// domain/model/Categoria.java
package com.restaurante.domain.model;

public class Categoria {
    private Long id;
    private String nombre;
    private String descripcion;

    public Categoria() {}

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y setters...
}
```

```java
// domain/model/Plato.java
package com.restaurante.domain.model;

import java.math.BigDecimal;

public class Plato {
    private Long id;
    private Long categoriaId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private boolean disponible;

    public Plato() {}

    public Plato(Long categoriaId, String nombre, BigDecimal precio) {
        this.categoriaId = categoriaId;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = true;
    }

    public void marcarNoDisponible() {
        this.disponible = false;
    }

    // Getters y setters...
}
```

```java
// domain/model/Pedido.java
package com.restaurante.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id;
    private Long mesaId;
    private Long clienteId;
    private Long usuarioId;
    private LocalDateTime fecha;
    private String estado;   // activo, preparacion, entregado, pagado, cancelado
    private BigDecimal total;
    private List<DetallePedido> detalle = new ArrayList<>();

    public void agregarDetalle(DetallePedido item) {
        this.detalle.add(item);
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.total = detalle.stream()
            .map(DetallePedido::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cerrar() {
        this.estado = "pagado";
    }

    public void cancelar() {
        this.estado = "cancelado";
    }

    // Getters y setters...
}
```

```java
// domain/model/DetallePedido.java
package com.restaurante.domain.model;

import java.math.BigDecimal;

public class DetallePedido {
    private Long id;
    private Long pedidoId;
    private Long platoId;
    private String nombrePlato;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetallePedido(Long platoId, String nombrePlato, int cantidad, BigDecimal precioUnitario) {
        this.platoId = platoId;
        this.nombrePlato = nombrePlato;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    // Getters y setters...
}
```

```java
// domain/model/Mesa.java
package com.restaurante.domain.model;

public class Mesa {
    private Long id;
    private int numero;
    private int capacidad;
    private String ubicacion;
    private String estado;   // libre, ocupada, reservada

    public void ocupar() {
        this.estado = "ocupada";
    }

    public void liberar() {
        this.estado = "libre";
    }

    // Getters y setters...
}
```

```java
// domain/model/Factura.java
package com.restaurante.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Factura {
    private Long id;
    private Long pedidoId;
    private BigDecimal monto;
    private String metodoPago;  // efectivo, tarjeta, transferencia
    private LocalDateTime fecha;

    // Getters y setters...
}
```

```java
// domain/model/Cliente.java
package com.restaurante.domain.model;

public class Cliente {
    private Long id;
    private String nombre;
    private String telefono;
    private String email;

    // Getters y setters...
}
```

```java
// domain/model/Usuario.java
package com.restaurante.domain.model;

public class Usuario {
    private Long id;
    private String nombre;
    private String rol;     // admin, mesero, cocina, caja
    private String usuario;
    private String contrasenaHash;

    public boolean esAdmin() {
        return "admin".equals(this.rol);
    }

    // Getters y setters...
}
```

### 3.2 Puertos de Entrada (Casos de Uso)

Los puertos de entrada definen las operaciones que el sistema expone. Son interfaces en el dominio.

```java
// domain/port/input/GestionMenuUseCase.java
package com.restaurante.domain.port.input;

import com.restaurante.domain.model.Categoria;
import com.restaurante.domain.model.Plato;
import java.util.List;
import java.util.Optional;

public interface GestionMenuUseCase {
    // Categorías
    Categoria crearCategoria(String nombre, String descripcion);
    List<Categoria> listarCategorias();
    Optional<Categoria> buscarCategoriaPorId(Long id);
    Categoria actualizarCategoria(Long id, String nombre, String descripcion);
    void eliminarCategoria(Long id);

    // Platos
    Plato crearPlato(Long categoriaId, String nombre, String descripcion, BigDecimal precio);
    List<Plato> listarPlatos();
    List<Plato> listarPlatosPorCategoria(Long categoriaId);
    Optional<Plato> buscarPlatoPorId(Long id);
    Plato actualizarPlato(Long id, Long categoriaId, String nombre, BigDecimal precio);
    void cambiarDisponibilidadPlato(Long id, boolean disponible);
    void eliminarPlato(Long id);
}
```

```java
// domain/port/input/TomarPedidoUseCase.java
package com.restaurante.domain.port.input;

import com.restaurante.domain.model.Pedido;
import com.restaurante.domain.model.DetallePedido;
import java.util.List;
import java.util.Optional;

public interface TomarPedidoUseCase {
    Pedido crearPedido(Long mesaId, Long clienteId, Long usuarioId);
    void agregarPlatoAPedido(Long pedidoId, Long platoId, int cantidad);
    void quitarPlatoDePedido(Long pedidoId, Long detalleId);
    List<Pedido> listarPedidosActivos();
    List<Pedido> listarPedidosPorMesa(Long mesaId);
    Optional<Pedido> buscarPedidoPorId(Long id);
    void marcarEnPreparacion(Long pedidoId);
    void marcarEntregado(Long pedidoId);
    void cancelarPedido(Long pedidoId);
}
```

```java
// domain/port/input/GestionMesasUseCase.java
package com.restaurante.domain.port.input;

import com.restaurante.domain.model.Mesa;
import java.util.List;
import java.util.Optional;

public interface GestionMesasUseCase {
    Mesa crearMesa(int numero, int capacidad, String ubicacion);
    List<Mesa> listarMesas();
    List<Mesa> listarMesasLibres();
    Optional<Mesa> buscarMesaPorId(Long id);
    Optional<Mesa> buscarMesaPorNumero(int numero);
    void ocuparMesa(Long id);
    void liberarMesa(Long id);
    void eliminarMesa(Long id);
}
```

```java
// domain/port/input/FacturacionUseCase.java
package com.restaurante.domain.port.input;

import com.restaurante.domain.model.Factura;
import java.util.List;
import java.util.Optional;

public interface FacturacionUseCase {
    Factura generarFactura(Long pedidoId, String metodoPago);
    Optional<Factura> buscarFacturaPorId(Long id);
    Optional<Factura> buscarFacturaPorPedido(Long pedidoId);
    List<Factura> listarFacturas();
}
```

```java
// domain/port/input/AutenticacionUseCase.java
package com.restaurante.domain.port.input;

import com.restaurante.domain.model.Usuario;
import java.util.Optional;

public interface AutenticacionUseCase {
    Optional<Usuario> login(String usuario, String contrasena);
    Usuario registrarUsuario(String nombre, String rol, String usuario, String contrasena);
    List<Usuario> listarUsuarios();
}
```

### 3.3 Puertos de Salida (Repositorios)

Definen cómo el dominio persiste y recupera datos. El dominio no sabe cómo se implementan.

```java
// domain/port/output/CategoriaRepository.java
package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {
    Categoria guardar(Categoria categoria);
    Optional<Categoria> buscarPorId(Long id);
    List<Categoria> listarTodos();
    void eliminarPorId(Long id);
}
```

```java
// domain/port/output/PlatoRepository.java
package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Plato;
import java.util.List;
import java.util.Optional;

public interface PlatoRepository {
    Plato guardar(Plato plato);
    Optional<Plato> buscarPorId(Long id);
    List<Plato> listarTodos();
    List<Plato> listarPorCategoria(Long categoriaId);
    List<Plato> listarDisponibles();
    void eliminarPorId(Long id);
}
```

```java
// domain/port/output/MesaRepository.java
package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Mesa;
import java.util.List;
import java.util.Optional;

public interface MesaRepository {
    Mesa guardar(Mesa mesa);
    Optional<Mesa> buscarPorId(Long id);
    Optional<Mesa> buscarPorNumero(int numero);
    List<Mesa> listarTodos();
    List<Mesa> listarPorEstado(String estado);
    void eliminarPorId(Long id);
}
```

```java
// domain/port/output/PedidoRepository.java
package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoRepository {
    Pedido guardar(Pedido pedido);
    Optional<Pedido> buscarPorId(Long id);
    List<Pedido> listarTodos();
    List<Pedido> listarPorEstado(String estado);
    List<Pedido> listarPorMesa(Long mesaId);
}
```

```java
// domain/port/output/ClienteRepository.java
package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    List<Cliente> listarTodos();
    Optional<Cliente> buscarPorTelefono(String telefono);
}
```

```java
// domain/port/output/UsuarioRepository.java
package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorUsuario(String usuario);
    List<Usuario> listarTodos();
}
```

```java
// domain/port/output/FacturaRepository.java
package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Factura;
import java.util.List;
import java.util.Optional;

public interface FacturaRepository {
    Factura guardar(Factura factura);
    Optional<Factura> buscarPorId(Long id);
    Optional<Factura> buscarPorPedidoId(Long pedidoId);
    List<Factura> listarTodos();
}
```

---

## 4. Capa de Aplicación — Casos de Uso (Services)

Los servicios implementan los puertos de entrada usando los puertos de salida. Solo dependen de interfaces, nunca de implementaciones concretas (**Inyección de Dependencias**).

```java
// domain/service/GestionMenuService.java
package com.restaurante.domain.service;

import com.restaurante.domain.model.Categoria;
import com.restaurante.domain.model.Plato;
import com.restaurante.domain.port.input.GestionMenuUseCase;
import com.restaurante.domain.port.output.CategoriaRepository;
import com.restaurante.domain.port.output.PlatoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class GestionMenuService implements GestionMenuUseCase {

    private final CategoriaRepository categoriaRepository;
    private final PlatoRepository platoRepository;

    public GestionMenuService(CategoriaRepository categoriaRepository,
                              PlatoRepository platoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.platoRepository = platoRepository;
    }

    @Override
    public Categoria crearCategoria(String nombre, String descripcion) {
        Categoria categoria = new Categoria(nombre, descripcion);
        return categoriaRepository.guardar(categoria);
    }

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.listarTodos();
    }

    @Override
    public Optional<Categoria> buscarCategoriaPorId(Long id) {
        return categoriaRepository.buscarPorId(id);
    }

    @Override
    public Categoria actualizarCategoria(Long id, String nombre, String descripcion) {
        Categoria categoria = categoriaRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + id));
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        return categoriaRepository.guardar(categoria);
    }

    @Override
    public void eliminarCategoria(Long id) {
        categoriaRepository.eliminarPorId(id);
    }

    @Override
    public Plato crearPlato(Long categoriaId, String nombre, String descripcion, BigDecimal precio) {
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (!categoriaRepository.buscarPorId(categoriaId).isPresent()) {
            throw new IllegalArgumentException("Categoría no existe: " + categoriaId);
        }
        Plato plato = new Plato(categoriaId, nombre, precio);
        plato.setDescripcion(descripcion);
        return platoRepository.guardar(plato);
    }

    @Override
    public List<Plato> listarPlatos() {
        return platoRepository.listarTodos();
    }

    @Override
    public List<Plato> listarPlatosPorCategoria(Long categoriaId) {
        return platoRepository.listarPorCategoria(categoriaId);
    }

    @Override
    public Optional<Plato> buscarPlatoPorId(Long id) {
        return platoRepository.buscarPorId(id);
    }

    @Override
    public Plato actualizarPlato(Long id, Long categoriaId, String nombre, BigDecimal precio) {
        Plato plato = platoRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado: " + id));
        plato.setCategoriaId(categoriaId);
        plato.setNombre(nombre);
        plato.setPrecio(precio);
        return platoRepository.guardar(plato);
    }

    @Override
    public void cambiarDisponibilidadPlato(Long id, boolean disponible) {
        Plato plato = platoRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado: " + id));
        if (disponible) {
            plato.setDisponible(true);
        } else {
            plato.marcarNoDisponible();
        }
        platoRepository.guardar(plato);
    }

    @Override
    public void eliminarPlato(Long id) {
        platoRepository.eliminarPorId(id);
    }
}
```

```java
// domain/service/TomarPedidoService.java
package com.restaurante.domain.service;

import com.restaurante.domain.model.*;
import com.restaurante.domain.port.input.TomarPedidoUseCase;
import com.restaurante.domain.port.output.PedidoRepository;
import com.restaurante.domain.port.output.PlatoRepository;
import com.restaurante.domain.port.output.MesaRepository;

import java.util.List;
import java.util.Optional;

public class TomarPedidoService implements TomarPedidoUseCase {

    private final PedidoRepository pedidoRepository;
    private final PlatoRepository platoRepository;
    private final MesaRepository mesaRepository;

    public TomarPedidoService(PedidoRepository pedidoRepository,
                              PlatoRepository platoRepository,
                              MesaRepository mesaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.platoRepository = platoRepository;
        this.mesaRepository = mesaRepository;
    }

    @Override
    public Pedido crearPedido(Long mesaId, Long clienteId, Long usuarioId) {
        Mesa mesa = mesaRepository.buscarPorId(mesaId)
            .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada: " + mesaId));

        Pedido pedido = new Pedido();
        pedido.setMesaId(mesaId);
        pedido.setClienteId(clienteId);
        pedido.setUsuarioId(usuarioId);
        pedido.setEstado("activo");
        pedido.setTotal(BigDecimal.ZERO);

        mesa.ocupar();
        mesaRepository.guardar(mesa);

        return pedidoRepository.guardar(pedido);
    }

    @Override
    public void agregarPlatoAPedido(Long pedidoId, Long platoId, int cantidad) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId));

        Plato plato = platoRepository.buscarPorId(platoId)
            .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado: " + platoId));

        if (!plato.isDisponible()) {
            throw new IllegalStateException("El plato " + plato.getNombre() + " no está disponible");
        }

        DetallePedido detalle = new DetallePedido(
            platoId, plato.getNombre(), cantidad, plato.getPrecio()
        );
        pedido.agregarDetalle(detalle);
        pedidoRepository.guardar(pedido);
    }

    @Override
    public void quitarPlatoDePedido(Long pedidoId, Long detalleId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId));
        pedido.getDetalle().removeIf(d -> d.getId().equals(detalleId));
        pedido.recalcularTotal();
        pedidoRepository.guardar(pedido);
    }

    @Override
    public List<Pedido> listarPedidosActivos() {
        return pedidoRepository.listarPorEstado("activo");
    }

    @Override
    public List<Pedido> listarPedidosPorMesa(Long mesaId) {
        return pedidoRepository.listarPorMesa(mesaId);
    }

    @Override
    public Optional<Pedido> buscarPedidoPorId(Long id) {
        return pedidoRepository.buscarPorId(id);
    }

    @Override
    public void marcarEnPreparacion(Long pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId));
        pedido.setEstado("preparacion");
        pedidoRepository.guardar(pedido);
    }

    @Override
    public void marcarEntregado(Long pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId));
        pedido.setEstado("entregado");
        pedidoRepository.guardar(pedido);
    }

    @Override
    public void cancelarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId));
        pedido.cancelar();

        Mesa mesa = mesaRepository.buscarPorId(pedido.getMesaId()).orElse(null);
        if (mesa != null) {
            mesa.liberar();
            mesaRepository.guardar(mesa);
        }

        pedidoRepository.guardar(pedido);
    }
}
```

```java
// domain/service/GestionMesasService.java
package com.restaurante.domain.service;

import com.restaurante.domain.model.Mesa;
import com.restaurante.domain.port.input.GestionMesasUseCase;
import com.restaurante.domain.port.output.MesaRepository;

import java.util.List;
import java.util.Optional;

public class GestionMesasService implements GestionMesasUseCase {

    private final MesaRepository mesaRepository;

    public GestionMesasService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    @Override
    public Mesa crearMesa(int numero, int capacidad, String ubicacion) {
        if (mesaRepository.buscarPorNumero(numero).isPresent()) {
            throw new IllegalArgumentException("Ya existe una mesa con el número: " + numero);
        }
        Mesa mesa = new Mesa();
        mesa.setNumero(numero);
        mesa.setCapacidad(capacidad);
        mesa.setUbicacion(ubicacion);
        mesa.setEstado("libre");
        return mesaRepository.guardar(mesa);
    }

    @Override
    public List<Mesa> listarMesas() {
        return mesaRepository.listarTodos();
    }

    @Override
    public List<Mesa> listarMesasLibres() {
        return mesaRepository.listarPorEstado("libre");
    }

    @Override
    public Optional<Mesa> buscarMesaPorId(Long id) {
        return mesaRepository.buscarPorId(id);
    }

    @Override
    public Optional<Mesa> buscarMesaPorNumero(int numero) {
        return mesaRepository.buscarPorNumero(numero);
    }

    @Override
    public void ocuparMesa(Long id) {
        Mesa mesa = mesaRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada: " + id));
        mesa.ocupar();
        mesaRepository.guardar(mesa);
    }

    @Override
    public void liberarMesa(Long id) {
        Mesa mesa = mesaRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada: " + id));
        mesa.liberar();
        mesaRepository.guardar(mesa);
    }

    @Override
    public void eliminarMesa(Long id) {
        mesaRepository.eliminarPorId(id);
    }
}
```

```java
// domain/service/FacturacionService.java
package com.restaurante.domain.service;

import com.restaurante.domain.model.Factura;
import com.restaurante.domain.model.Pedido;
import com.restaurante.domain.port.input.FacturacionUseCase;
import com.restaurante.domain.port.output.FacturaRepository;
import com.restaurante.domain.port.output.PedidoRepository;
import com.restaurante.domain.port.output.MesaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FacturacionService implements FacturacionUseCase {

    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;

    public FacturacionService(FacturaRepository facturaRepository,
                              PedidoRepository pedidoRepository,
                              MesaRepository mesaRepository) {
        this.facturaRepository = facturaRepository;
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
    }

    @Override
    public Factura generarFactura(Long pedidoId, String metodoPago) {
        Pedido pedido = pedidoRepository.buscarPorId(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado: " + pedidoId));

        if ("pagado".equals(pedido.getEstado())) {
            throw new IllegalStateException("El pedido ya fue pagado");
        }

        Factura factura = new Factura();
        factura.setPedidoId(pedidoId);
        factura.setMonto(pedido.getTotal());
        factura.setMetodoPago(metodoPago);
        factura.setFecha(LocalDateTime.now());

        pedido.cerrar();
        pedidoRepository.guardar(pedido);

        mesaRepository.buscarPorId(pedido.getMesaId())
            .ifPresent(mesa -> {
                mesa.liberar();
                mesaRepository.guardar(mesa);
            });

        return facturaRepository.guardar(factura);
    }

    @Override
    public Optional<Factura> buscarFacturaPorId(Long id) {
        return facturaRepository.buscarPorId(id);
    }

    @Override
    public Optional<Factura> buscarFacturaPorPedido(Long pedidoId) {
        return facturaRepository.buscarPorPedidoId(pedidoId);
    }

    @Override
    public List<Factura> listarFacturas() {
        return facturaRepository.listarTodos();
    }
}
```

```java
// domain/service/AutenticacionService.java
package com.restaurante.domain.service;

import com.restaurante.domain.model.Usuario;
import com.restaurante.domain.port.input.AutenticacionUseCase;
import com.restaurante.domain.port.output.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class AutenticacionService implements AutenticacionUseCase {

    private final UsuarioRepository usuarioRepository;

    public AutenticacionService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Usuario> login(String usuario, String contrasena) {
        return usuarioRepository.buscarPorUsuario(usuario)
            .filter(u -> verificarContrasena(contrasena, u.getContrasenaHash()));
    }

    @Override
    public Usuario registrarUsuario(String nombre, String rol, String usuario, String contrasena) {
        if (usuarioRepository.buscarPorUsuario(usuario).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe: " + usuario);
        }
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setRol(rol);
        u.setUsuario(usuario);
        u.setContrasenaHash(hashContrasena(contrasena));
        return usuarioRepository.guardar(u);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }

    private boolean verificarContrasena(String raw, String hash) {
        // Implementar con BCrypt
        return true; // Placeholder
    }

    private String hashContrasena(String contrasena) {
        // Implementar con BCrypt
        return contrasena; // Placeholder
    }
}
```

---

## 5. Capa de Infraestructura

### 5.1 Adaptadores de Persistencia (JPA)

Las entidades JPA son **diferentes** de las entidades de dominio. Se mapean mediante un mapper.

```java
// infrastructure/adapter/outbound/persistence/entity/CategoriaEntity.java
package com.restaurante.infrastructure.adapter.outbound.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categorias")
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Getters y setters...
}
```

```java
// infrastructure/adapter/outbound/persistence/entity/PlatoEntity.java
package com.restaurante.infrastructure.adapter.outbound.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "platos")
public class PlatoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Boolean disponible = true;

    // Getters y setters...
}
```

```java
// infrastructure/adapter/outbound/persistence/entity/PedidoEntity.java
package com.restaurante.infrastructure.adapter.outbound.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mesa_id", nullable = false)
    private Long mesaId;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    private LocalDateTime fecha;

    @Column(length = 20)
    private String estado = "activo";

    @Column(precision = 10, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "pedidoId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedidoEntity> detalle = new ArrayList<>();

    // Getters y setters...
}
```

```java
// infrastructure/adapter/outbound/persistence/mapper/CategoriaEntityMapper.java
package com.restaurante.infrastructure.adapter.outbound.persistence.mapper;

import com.restaurante.domain.model.Categoria;
import com.restaurante.infrastructure.adapter.outbound.persistence.entity.CategoriaEntity;

public class CategoriaEntityMapper {

    public static Categoria toDomain(CategoriaEntity entity) {
        Categoria domain = new Categoria();
        domain.setId(entity.getId());
        domain.setNombre(entity.getNombre());
        domain.setDescripcion(entity.getDescripcion());
        return domain;
    }

    public static CategoriaEntity toEntity(Categoria domain) {
        CategoriaEntity entity = new CategoriaEntity();
        entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setDescripcion(domain.getDescripcion());
        return entity;
    }
}
```

```java
// infrastructure/adapter/outbound/persistence/adapter/JpaCategoriaRepositoryAdapter.java
package com.restaurante.infrastructure.adapter.outbound.persistence.adapter;

import com.restaurante.domain.model.Categoria;
import com.restaurante.domain.port.output.CategoriaRepository;
import com.restaurante.infrastructure.adapter.outbound.persistence.entity.CategoriaEntity;
import com.restaurante.infrastructure.adapter.outbound.persistence.jpa.SpringDataCategoriaRepository;
import com.restaurante.infrastructure.adapter.outbound.persistence.mapper.CategoriaEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaCategoriaRepositoryAdapter implements CategoriaRepository {

    private final SpringDataCategoriaRepository springRepo;

    public JpaCategoriaRepositoryAdapter(SpringDataCategoriaRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        CategoriaEntity entity = CategoriaEntityMapper.toEntity(categoria);
        CategoriaEntity saved = springRepo.save(entity);
        return CategoriaEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        return springRepo.findById(id)
            .map(CategoriaEntityMapper::toDomain);
    }

    @Override
    public List<Categoria> listarTodos() {
        return springRepo.findAll().stream()
            .map(CategoriaEntityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void eliminarPorId(Long id) {
        springRepo.deleteById(id);
    }
}
```

### 5.2 Adaptadores de Entrada (REST Controllers)

```java
// infrastructure/adapter/inbound/rest/CategoriaController.java
package com.restaurante.infrastructure.adapter.inbound.rest;

import com.restaurante.domain.model.Categoria;
import com.restaurante.domain.port.input.GestionMenuUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final GestionMenuUseCase gestionMenu;

    public CategoriaController(GestionMenuUseCase gestionMenu) {
        this.gestionMenu = gestionMenu;
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody CrearCategoriaRequest request) {
        Categoria categoria = gestionMenu.crearCategoria(request.getNombre(), request.getDescripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(gestionMenu.listarCategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscar(@PathVariable Long id) {
        return gestionMenu.buscarCategoriaPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @RequestBody CrearCategoriaRequest request) {
        Categoria categoria = gestionMenu.actualizarCategoria(id, request.getNombre(), request.getDescripcion());
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gestionMenu.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
```

```java
// infrastructure/adapter/inbound/rest/PedidoController.java
package com.restaurante.infrastructure.adapter.inbound.rest;

import com.restaurante.domain.model.Pedido;
import com.restaurante.domain.port.input.TomarPedidoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final TomarPedidoUseCase tomarPedido;

    public PedidoController(TomarPedidoUseCase tomarPedido) {
        this.tomarPedido = tomarPedido;
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody CrearPedidoRequest request) {
        Pedido pedido = tomarPedido.crearPedido(
            request.getMesaId(), request.getClienteId(), request.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PostMapping("/{id}/platos")
    public ResponseEntity<Void> agregarPlato(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Long platoId = Long.valueOf(body.get("platoId").toString());
        int cantidad = Integer.parseInt(body.get("cantidad").toString());
        tomarPedido.agregarPlatoAPedido(id, platoId, cantidad);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Pedido>> listarActivos() {
        return ResponseEntity.ok(tomarPedido.listarPedidosActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return tomarPedido.buscarPedidoPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/preparacion")
    public ResponseEntity<Void> marcarPreparacion(@PathVariable Long id) {
        tomarPedido.marcarEnPreparacion(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/entregar")
    public ResponseEntity<Void> marcarEntregado(@PathVariable Long id) {
        tomarPedido.marcarEntregado(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        tomarPedido.cancelarPedido(id);
        return ResponseEntity.ok().build();
    }
}
```

---

## 6. Configuración Spring Boot

### pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>

    <groupId>com.restaurante</groupId>
    <artifactId>api-restaurante</artifactId>
    <version>1.0.0</version>
    <name>Api Restaurante</name>

    <properties>
        <java.version>17</java.version>
    </properties>

    <dependencies>
        <!-- Web -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- JPA -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- PostgreSQL -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- Validation -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>

        <!-- JWT -->
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.3</version>
        </dependency>

        <!-- OpenAPI -->
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.3.0</version>
        </dependency>

        <!-- Flyway -->
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-core</artifactId>
        </dependency>

        <!-- Test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/restaurante
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true

  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true

server:
  port: 8080

jwt:
  secret: ${JWT_SECRET:cambiar_en_produccion}
  expiration: 86400000
```

### Main Application (Inyección de Dependencias)

```java
// ApiRestauranteApplication.java
package com.restaurante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiRestauranteApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiRestauranteApplication.class, args);
    }
}
```

```java
// infrastructure/config/BeanConfiguration.java
package com.restaurante.infrastructure.config;

import com.restaurante.domain.port.input.*;
import com.restaurante.domain.port.output.*;
import com.restaurante.domain.service.*;
import com.restaurante.infrastructure.adapter.outbound.persistence.adapter.*;
import com.restaurante.infrastructure.adapter.outbound.persistence.jpa.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public CategoriaRepository categoriaRepository(SpringDataCategoriaRepository repo) {
        return new JpaCategoriaRepositoryAdapter(repo);
    }

    @Bean
    public PlatoRepository platoRepository(SpringDataPlatoRepository repo) {
        return new JpaPlatoRepositoryAdapter(repo);
    }

    @Bean
    public MesaRepository mesaRepository(SpringDataMesaRepository repo) {
        return new JpaMesaRepositoryAdapter(repo);
    }

    @Bean
    public PedidoRepository pedidoRepository(SpringDataPedidoRepository repo) {
        return new JpaPedidoRepositoryAdapter(repo);
    }

    @Bean
    public FacturaRepository facturaRepository(SpringDataFacturaRepository repo) {
        return new JpaFacturaRepositoryAdapter(repo);
    }

    @Bean
    public ClienteRepository clienteRepository(SpringDataClienteRepository repo) {
        return new JpaClienteRepositoryAdapter(repo);
    }

    @Bean
    public UsuarioRepository usuarioRepository(SpringDataUsuarioRepository repo) {
        return new JpaUsuarioRepositoryAdapter(repo);
    }

    @Bean
    public GestionMenuUseCase gestionMenuUseCase(
            CategoriaRepository categoriaRepo, PlatoRepository platoRepo) {
        return new GestionMenuService(categoriaRepo, platoRepo);
    }

    @Bean
    public TomarPedidoUseCase tomarPedidoUseCase(
            PedidoRepository pedidoRepo, PlatoRepository platoRepo, MesaRepository mesaRepo) {
        return new TomarPedidoService(pedidoRepo, platoRepo, mesaRepo);
    }

    @Bean
    public GestionMesasUseCase gestionMesasUseCase(MesaRepository mesaRepo) {
        return new GestionMesasService(mesaRepo);
    }

    @Bean
    public FacturacionUseCase facturacionUseCase(
            FacturaRepository facturaRepo, PedidoRepository pedidoRepo, MesaRepository mesaRepo) {
        return new FacturacionService(facturaRepo, pedidoRepo, mesaRepo);
    }

    @Bean
    public AutenticacionUseCase autenticacionUseCase(UsuarioRepository usuarioRepo) {
        return new AutenticacionService(usuarioRepo);
    }
}
```

---

## 7. Flujo Completo: "Crear Pedido"

```
Cliente HTTP
     │
     ▼
POST /api/pedidos  { mesaId: 1, clienteId: 5, usuarioId: 2 }
     │
     ▼
PedidoController.crear(request)
     │  (Adapter IN - REST)
     ▼
TomarPedidoUseCase.crearPedido(1, 5, 2)
     │  (Port IN - interface)
     ▼
TomarPedidoService.crearPedido(1, 5, 2)
     │  (Domain Service - implementación)
     │
     ├─► MesaRepository.buscarPorId(1)
     │       │  (Port OUT - interface)
     │       ▼
     │   JpaMesaRepositoryAdapter → SpringDataMesaRepository → PostgreSQL
     │
     ├─► PedidoRepository.guardar(pedido)
     │       │  (Port OUT - interface)
     │       ▼
     │   JpaPedidoRepositoryAdapter → SpringDataPedidoRepository → PostgreSQL
     │
     └─► MesaRepository.guardar(mesa)
             │  (Port OUT - interface)
             ▼
         JpaMesaRepositoryAdapter → SpringDataMesaRepository → PostgreSQL
     │
     ▼
Response: 201 Created + Pedido JSON
     │
     ▼
Cliente HTTP recibe el pedido creado
```

---

## 8. Endpoints REST

| Método | Endpoint | Caso de Uso |
|--------|----------|-------------|
| POST | `/api/auth/login` | Autenticación |
| POST | `/api/auth/register` | Registrar usuario |
| GET | `/api/categorias` | Listar categorías |
| POST | `/api/categorias` | Crear categoría |
| GET | `/api/categorias/{id}` | Buscar categoría |
| PUT | `/api/categorias/{id}` | Actualizar categoría |
| DELETE | `/api/categorias/{id}` | Eliminar categoría |
| GET | `/api/platos` | Listar platos |
| GET | `/api/platos?categoriaId=1` | Platos por categoría |
| POST | `/api/platos` | Crear plato |
| PUT | `/api/platos/{id}` | Actualizar plato |
| PATCH | `/api/platos/{id}/disponibilidad` | Cambiar disponibilidad |
| DELETE | `/api/platos/{id}` | Eliminar plato |
| GET | `/api/mesas` | Listar mesas |
| GET | `/api/mesas/libres` | Mesas disponibles |
| POST | `/api/mesas` | Crear mesa |
| PATCH | `/api/mesas/{id}/ocupar` | Ocupar mesa |
| PATCH | `/api/mesas/{id}/liberar` | Liberar mesa |
| POST | `/api/pedidos` | Crear pedido |
| POST | `/api/pedidos/{id}/platos` | Agregar plato al pedido |
| GET | `/api/pedidos/activos` | Pedidos activos |
| GET | `/api/pedidos/{id}` | Buscar pedido |
| PATCH | `/api/pedidos/{id}/preparacion` | En preparación |
| PATCH | `/api/pedidos/{id}/entregar` | Entregado |
| POST | `/api/pedidos/{id}/cancelar` | Cancelar pedido |
| POST | `/api/pedidos/{id}/facturar` | Generar factura |
| GET | `/api/facturas` | Listar facturas |
| GET | `/api/facturas/{id}` | Buscar factura |
| GET | `/api/facturas/pedido/{pedidoId}` | Factura por pedido |
| POST | `/api/clientes` | Registrar cliente |
| GET | `/api/clientes` | Listar clientes |
