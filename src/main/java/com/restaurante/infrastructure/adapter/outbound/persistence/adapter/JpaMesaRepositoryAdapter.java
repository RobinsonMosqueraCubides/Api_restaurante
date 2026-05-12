package com.restaurante.infrastructure.adapter.outbound.persistence.adapter;

import com.restaurante.domain.model.Mesa;
import com.restaurante.domain.port.output.MesaRepository;
import com.restaurante.infrastructure.adapter.outbound.persistence.entity.MesaEntity;
import com.restaurante.infrastructure.adapter.outbound.persistence.jpa.SpringDataMesaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaMesaRepositoryAdapter implements MesaRepository {

    private final SpringDataMesaRepository springRepo;

    public JpaMesaRepositoryAdapter(SpringDataMesaRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Mesa guardar(Mesa mesa) {
        MesaEntity saved = springRepo.save(toEntity(mesa));
        return toDomain(saved);
    }

    @Override
    public Optional<Mesa> buscarPorId(Long id) {
        return springRepo.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Mesa> buscarPorNumero(int numero) {
        return springRepo.findByNumero(numero).map(this::toDomain);
    }

    @Override
    public List<Mesa> listarTodos() {
        return springRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Mesa> listarPorEstado(String estado) {
        return springRepo.findByEstado(estado).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void eliminarPorId(Long id) {
        springRepo.deleteById(id);
    }

    private Mesa toDomain(MesaEntity e) {
        Mesa d = new Mesa();
        d.setId(e.getId());
        d.setNumero(e.getNumero());
        d.setCapacidad(e.getCapacidad());
        d.setUbicacion(e.getUbicacion());
        d.setEstado(e.getEstado());
        return d;
    }

    private MesaEntity toEntity(Mesa d) {
        MesaEntity e = new MesaEntity();
        e.setId(d.getId());
        e.setNumero(d.getNumero());
        e.setCapacidad(d.getCapacidad());
        e.setUbicacion(d.getUbicacion());
        e.setEstado(d.getEstado());
        return e;
    }
}
