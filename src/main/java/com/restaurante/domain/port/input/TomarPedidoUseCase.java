package com.restaurante.domain.port.input;

import com.restaurante.domain.model.Pedido;
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
