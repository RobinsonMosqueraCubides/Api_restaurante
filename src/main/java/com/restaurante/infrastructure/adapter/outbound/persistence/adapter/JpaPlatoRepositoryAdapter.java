package com.restaurante.infrastructure.adapter.outbound.persistence.adapter;

import com.restaurante.domain.model.Plato;
import com.restaurante.domain.port.output.PlatoRepository;
import com.restaurante.infrastructure.adapter.outbound.persistence.entity.PlatoEntity;
import com.restaurante.infrastructure.adapter.outbound.persistence.jpa.SpringDataPlatoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaPlatoRepositoryAdapter implements PlatoRepository {

    private final SpringDataPlatoRepository springRepo;

    public JpaPlatoRepositoryAdapter(SpringDataPlatoRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Plato guardar(Plato plato) {
        PlatoEntity saved = springRepo.save(toEntity(plato));
        return toDomain(saved);
    }

    @Override
    public Optional<Plato> buscarPorId(Long id) {
        return springRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Plato> listarTodos() {
        return springRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Plato> listarPorCategoria(Long categoriaId) {
        return springRepo.findByCategoriaId(categoriaId).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Plato> listarDisponibles() {
        return springRepo.findByDisponibleTrue().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void eliminarPorId(Long id) {
        springRepo.deleteById(id);
    }

    private Plato toDomain(PlatoEntity e) {
        Plato d = new Plato();
        d.setId(e.getId());
        d.setCategoriaId(e.getCategoriaId());
        d.setNombre(e.getNombre());
        d.setDescripcion(e.getDescripcion());
        d.setPrecio(e.getPrecio());
        d.setDisponible(e.getDisponible());
        return d;
    }

    private PlatoEntity toEntity(Plato d) {
        PlatoEntity e = new PlatoEntity();
        e.setId(d.getId());
        e.setCategoriaId(d.getCategoriaId());
        e.setNombre(d.getNombre());
        e.setDescripcion(d.getDescripcion());
        e.setPrecio(d.getPrecio());
        e.setDisponible(d.isDisponible());
        return e;
    }
}
