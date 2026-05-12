package com.restaurante.infrastructure.adapter.outbound.persistence.jpa;

import com.restaurante.infrastructure.adapter.outbound.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataClienteRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByTelefono(String telefono);
}
