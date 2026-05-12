package com.restaurante.infrastructure.adapter.outbound.persistence.adapter;

import com.restaurante.domain.model.Factura;
import com.restaurante.domain.port.output.FacturaRepository;
import com.restaurante.infrastructure.adapter.outbound.persistence.entity.FacturaEntity;
import com.restaurante.infrastructure.adapter.outbound.persistence.jpa.SpringDataFacturaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaFacturaRepositoryAdapter implements FacturaRepository {

    private final SpringDataFacturaRepository springRepo;

    public JpaFacturaRepositoryAdapter(SpringDataFacturaRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Factura guardar(Factura factura) {
        FacturaEntity saved = springRepo.save(toEntity(factura));
        return toDomain(saved);
    }

    @Override
    public Optional<Factura> buscarPorId(Long id) {
        return springRepo.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Factura> buscarPorPedidoId(Long pedidoId) {
        return springRepo.findByPedidoId(pedidoId).map(this::toDomain);
    }

    @Override
    public List<Factura> listarTodos() {
        return springRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Factura toDomain(FacturaEntity e) {
        Factura d = new Factura();
        d.setId(e.getId());
        d.setPedidoId(e.getPedidoId());
        d.setMonto(e.getMonto());
        d.setMetodoPago(e.getMetodoPago());
        d.setFecha(e.getFecha());
        return d;
    }

    private FacturaEntity toEntity(Factura d) {
        FacturaEntity e = new FacturaEntity();
        e.setId(d.getId());
        e.setPedidoId(d.getPedidoId());
        e.setMonto(d.getMonto());
        e.setMetodoPago(d.getMetodoPago());
        e.setFecha(d.getFecha());
        return e;
    }
}
