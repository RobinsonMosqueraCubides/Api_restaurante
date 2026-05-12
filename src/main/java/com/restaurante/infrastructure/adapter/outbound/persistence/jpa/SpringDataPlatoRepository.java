package com.restaurante.infrastructure.adapter.outbound.persistence.jpa;

import com.restaurante.infrastructure.adapter.outbound.persistence.entity.PlatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataPlatoRepository extends JpaRepository<PlatoEntity, Long> {
    List<PlatoEntity> findByCategoriaId(Long categoriaId);
    List<PlatoEntity> findByDisponibleTrue();
}
