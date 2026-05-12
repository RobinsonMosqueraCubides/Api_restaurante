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
