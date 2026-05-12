package com.restaurante.infrastructure.adapter.outbound.persistence.adapter;

import com.restaurante.domain.model.Categoria;
import com.restaurante.domain.port.output.CategoriaRepository;
import com.restaurante.infrastructure.adapter.outbound.persistence.entity.CategoriaEntity;
import com.restaurante.infrastructure.adapter.outbound.persistence.jpa.SpringDataCategoriaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaCategoriaRepositoryAdapter implements CategoriaRepository {

    private final SpringDataCategoriaRepository springRepo;

    public JpaCategoriaRepositoryAdapter(SpringDataCategoriaRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Categoria guardar(Categoria categoria) {
        CategoriaEntity entity = toEntity(categoria);
        CategoriaEntity saved = springRepo.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        return springRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Categoria> listarTodos() {
        return springRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public void eliminarPorId(Long id) {
        springRepo.deleteById(id);
    }

    private Categoria toDomain(CategoriaEntity e) {
        Categoria d = new Categoria();
        d.setId(e.getId());
        d.setNombre(e.getNombre());
        d.setDescripcion(e.getDescripcion());
        return d;
    }

    private CategoriaEntity toEntity(Categoria d) {
        CategoriaEntity e = new CategoriaEntity();
        e.setId(d.getId());
        e.setNombre(d.getNombre());
        e.setDescripcion(d.getDescripcion());
        return e;
    }
}
