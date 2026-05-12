package com.restaurante.infrastructure.adapter.outbound.persistence.jpa;

import com.restaurante.infrastructure.adapter.outbound.persistence.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataCategoriaRepository extends JpaRepository<CategoriaEntity, Long> {
}
