package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {
    Categoria guardar(Categoria categoria);
    Optional<Categoria> buscarPorId(Long id);
    List<Categoria> listarTodos();
    void eliminarPorId(Long id);
}
