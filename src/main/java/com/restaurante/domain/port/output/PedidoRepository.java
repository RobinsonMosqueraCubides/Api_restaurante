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
