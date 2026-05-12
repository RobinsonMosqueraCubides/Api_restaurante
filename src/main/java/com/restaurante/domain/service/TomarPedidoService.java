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
