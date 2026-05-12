package com.restaurante.infrastructure.adapter.outbound.persistence.jpa;

import com.restaurante.infrastructure.adapter.outbound.persistence.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataPedidoRepository extends JpaRepository<PedidoEntity, Long> {
    List<PedidoEntity> findByEstado(String estado);
    List<PedidoEntity> findByMesaId(Long mesaId);
}
