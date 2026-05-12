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
