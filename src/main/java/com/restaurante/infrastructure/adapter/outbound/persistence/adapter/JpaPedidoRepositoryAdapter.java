package com.restaurante.infrastructure.adapter.outbound.persistence.adapter;

import com.restaurante.domain.model.DetallePedido;
import com.restaurante.domain.model.Pedido;
import com.restaurante.domain.port.output.PedidoRepository;
import com.restaurante.infrastructure.adapter.outbound.persistence.entity.DetallePedidoEntity;
import com.restaurante.infrastructure.adapter.outbound.persistence.entity.PedidoEntity;
import com.restaurante.infrastructure.adapter.outbound.persistence.jpa.SpringDataPedidoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaPedidoRepositoryAdapter implements PedidoRepository {

    private final SpringDataPedidoRepository springRepo;

    public JpaPedidoRepositoryAdapter(SpringDataPedidoRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        PedidoEntity saved = springRepo.save(toEntity(pedido));
        return toDomain(saved);
    }

    @Override
    public Optional<Pedido> buscarPorId(Long id) {
        return springRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Pedido> listarTodos() {
        return springRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Pedido> listarPorEstado(String estado) {
        return springRepo.findByEstado(estado).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Pedido> listarPorMesa(Long mesaId) {
        return springRepo.findByMesaId(mesaId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Pedido toDomain(PedidoEntity e) {
        Pedido d = new Pedido();
        d.setId(e.getId());
        d.setMesaId(e.getMesaId());
        d.setClienteId(e.getClienteId());
        d.setUsuarioId(e.getUsuarioId());
        d.setFecha(e.getFecha());
        d.setEstado(e.getEstado());
        d.setTotal(e.getTotal());
        if (e.getDetalle() != null) {
            d.setDetalle(e.getDetalle().stream().map(this::toDomainDetalle).collect(Collectors.toList()));
        }
        return d;
    }

    private PedidoEntity toEntity(Pedido d) {
        PedidoEntity e = new PedidoEntity();
        e.setId(d.getId());
        e.setMesaId(d.getMesaId());
        e.setClienteId(d.getClienteId());
        e.setUsuarioId(d.getUsuarioId());
        e.setFecha(d.getFecha());
        e.setEstado(d.getEstado());
        e.setTotal(d.getTotal());
        if (d.getDetalle() != null) {
            e.setDetalle(d.getDetalle().stream().map(this::toEntityDetalle).collect(Collectors.toList()));
        }
        return e;
    }

    private DetallePedido toDomainDetalle(DetallePedidoEntity e) {
        DetallePedido d = new DetallePedido();
        d.setId(e.getId());
        d.setPedidoId(e.getPedidoId());
        d.setPlatoId(e.getPlatoId());
        d.setCantidad(e.getCantidad());
        d.setSubtotal(e.getSubtotal());
        return d;
    }

    private DetallePedidoEntity toEntityDetalle(DetallePedido d) {
        DetallePedidoEntity e = new DetallePedidoEntity();
        e.setId(d.getId());
        e.setPedidoId(d.getPedidoId());
        e.setPlatoId(d.getPlatoId());
        e.setCantidad(d.getCantidad());
        e.setSubtotal(d.getSubtotal());
        return e;
    }
}
