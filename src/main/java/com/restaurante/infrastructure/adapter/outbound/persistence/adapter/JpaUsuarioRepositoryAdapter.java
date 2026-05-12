package com.restaurante.infrastructure.adapter.outbound.persistence.adapter;

import com.restaurante.domain.model.Usuario;
import com.restaurante.domain.port.output.UsuarioRepository;
import com.restaurante.infrastructure.adapter.outbound.persistence.entity.UsuarioEntity;
import com.restaurante.infrastructure.adapter.outbound.persistence.jpa.SpringDataUsuarioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaUsuarioRepositoryAdapter implements UsuarioRepository {

    private final SpringDataUsuarioRepository springRepo;

    public JpaUsuarioRepositoryAdapter(SpringDataUsuarioRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity saved = springRepo.save(toEntity(usuario));
        return toDomain(saved);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return springRepo.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorUsuario(String usuario) {
        return springRepo.findByUsuario(usuario).map(this::toDomain);
    }

    @Override
    public List<Usuario> listarTodos() {
        return springRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    private Usuario toDomain(UsuarioEntity e) {
        Usuario d = new Usuario();
        d.setId(e.getId());
        d.setNombre(e.getNombre());
        d.setRol(e.getRol());
        d.setUsuario(e.getUsuario());
        d.setContrasenaHash(e.getContrasenaHash());
        return d;
    }

    private UsuarioEntity toEntity(Usuario d) {
        UsuarioEntity e = new UsuarioEntity();
        e.setId(d.getId());
        e.setNombre(d.getNombre());
        e.setRol(d.getRol());
        e.setUsuario(d.getUsuario());
        e.setContrasenaHash(d.getContrasenaHash());
        return e;
    }
}
