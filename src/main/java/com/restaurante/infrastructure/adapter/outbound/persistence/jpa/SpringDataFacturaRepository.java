package com.restaurante.infrastructure.adapter.outbound.persistence.jpa;

import com.restaurante.infrastructure.adapter.outbound.persistence.entity.FacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataFacturaRepository extends JpaRepository<FacturaEntity, Long> {
    Optional<FacturaEntity> findByPedidoId(Long pedidoId);
}
