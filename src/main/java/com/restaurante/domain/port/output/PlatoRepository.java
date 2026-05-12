package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Plato;
import java.util.List;
import java.util.Optional;

public interface PlatoRepository {
    Plato guardar(Plato plato);
    Optional<Plato> buscarPorId(Long id);
    List<Plato> listarTodos();
    List<Plato> listarPorCategoria(Long categoriaId);
    List<Plato> listarDisponibles();
    void eliminarPorId(Long id);
}
