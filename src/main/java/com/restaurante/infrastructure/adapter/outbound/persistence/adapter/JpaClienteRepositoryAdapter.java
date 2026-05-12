package com.restaurante.infrastructure.adapter.outbound.persistence.adapter;

import com.restaurante.domain.model.Cliente;
import com.restaurante.domain.port.output.ClienteRepository;
import com.restaurante.infrastructure.adapter.outbound.persistence.entity.ClienteEntity;
import com.restaurante.infrastructure.adapter.outbound.persistence.jpa.SpringDataClienteRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaClienteRepositoryAdapter implements ClienteRepository {

    private final SpringDataClienteRepository springRepo;

    public JpaClienteRepositoryAdapter(SpringDataClienteRepository springRepo) {
        this.springRepo = springRepo;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteEntity saved = springRepo.save(toEntity(cliente));
        return toDomain(saved);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return springRepo.findById(id).map(this::toDomain);
    }

    @Override
    public List<Cliente> listarTodos() {
        return springRepo.findAll().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Cliente> buscarPorTelefono(String telefono) {
        return springRepo.findByTelefono(telefono).map(this::toDomain);
    }

    private Cliente toDomain(ClienteEntity e) {
        Cliente d = new Cliente();
        d.setId(e.getId());
        d.setNombre(e.getNombre());
        d.setTelefono(e.getTelefono());
        d.setEmail(e.getEmail());
        return d;
    }

    private ClienteEntity toEntity(Cliente d) {
        ClienteEntity e = new ClienteEntity();
        e.setId(d.getId());
        e.setNombre(d.getNombre());
        e.setTelefono(d.getTelefono());
        e.setEmail(d.getEmail());
        return e;
    }
}
