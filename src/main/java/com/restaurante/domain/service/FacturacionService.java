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
