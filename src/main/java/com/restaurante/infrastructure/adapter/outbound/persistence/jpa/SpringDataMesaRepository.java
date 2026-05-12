package com.restaurante.infrastructure.adapter.outbound.persistence.jpa;

import com.restaurante.infrastructure.adapter.outbound.persistence.entity.MesaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataMesaRepository extends JpaRepository<MesaEntity, Long> {
    Optional<MesaEntity> findByNumero(Integer numero);
    List<MesaEntity> findByEstado(String estado);
}
