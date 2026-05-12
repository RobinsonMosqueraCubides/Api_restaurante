package com.restaurante.domain.port.input;

import com.restaurante.domain.model.Categoria;
import com.restaurante.domain.model.Plato;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GestionMenuUseCase {
    Categoria crearCategoria(String nombre, String descripcion);
    List<Categoria> listarCategorias();
    Optional<Categoria> buscarCategoriaPorId(Long id);
    Categoria actualizarCategoria(Long id, String nombre, String descripcion);
    void eliminarCategoria(Long id);

    Plato crearPlato(Long categoriaId, String nombre, String descripcion, BigDecimal precio);
    List<Plato> listarPlatos();
    List<Plato> listarPlatosPorCategoria(Long categoriaId);
    Optional<Plato> buscarPlatoPorId(Long id);
    Plato actualizarPlato(Long id, Long categoriaId, String nombre, BigDecimal precio);
    void cambiarDisponibilidadPlato(Long id, boolean disponible);
    void eliminarPlato(Long id);
}
