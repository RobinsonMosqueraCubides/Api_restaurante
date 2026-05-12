# Plan de Accion - Base de Datos para Restaurante

## Entidades propuestas

- **Categorias**: Clasificacion de platos (entradas, platos fuertes, postres, bebidas)
- **Platos**: Menu del restaurante (nombre, descripcion, precio, categoria, disponible)
- **Mesas**: Gestion de mesas (numero, capacidad, ubicacion, estado)
- **Clientes**: Datos de los comensales (nombre, telefono, email)
- **Pedidos**: Registro de ordenes (mesa, cliente, fecha, estado, total)
- **DetallePedido**: Platos incluidos en cada pedido (plato, cantidad, subtotal)
- **Facturas**: Registro contable (pedido, monto, metodo pago, fecha)
- **Usuarios**: Personal del restaurante (nombre, rol, usuario, contraseña)

## Tecnologia sugerida

- **Motor**: PostgreSQL (relacional, robusto, gratuito)
- **API**: Node.js con Express o Python con FastAPI
- **ORM**: Prisma (Node) o SQLAlchemy (Python)

## Diagrama relacional basico

```
Categorias 1---* Platos
Clientes  1---* Pedidos
Mesas     1---* Pedidos
Pedidos   1---* DetallePedido
Platos    1---* DetallePedido
Pedidos   1---1 Facturas
Usuarios  (independiente, para autenticacion)
```
