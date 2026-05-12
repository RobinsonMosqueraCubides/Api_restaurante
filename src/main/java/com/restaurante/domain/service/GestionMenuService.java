package com.restaurante.domain.service;

import com.restaurante.domain.model.Categoria;
import com.restaurante.domain.model.Plato;
import com.restaurante.domain.port.input.GestionMenuUseCase;
import com.restaurante.domain.port.output.CategoriaRepository;
import com.restaurante.domain.port.output.PlatoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class GestionMenuService implements GestionMenuUseCase {

    private final CategoriaRepository categoriaRepository;
    private final PlatoRepository platoRepository;

    public GestionMenuService(CategoriaRepository categoriaRepository,
                              PlatoRepository platoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.platoRepository = platoRepository;
    }

    @Override
    public Categoria crearCategoria(String nombre, String descripcion) {
        Categoria categoria = new Categoria(nombre, descripcion);
        return categoriaRepository.guardar(categoria);
    }

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.listarTodos();
    }

    @Override
    public Optional<Categoria> buscarCategoriaPorId(Long id) {
        return categoriaRepository.buscarPorId(id);
    }

    @Override
    public Categoria actualizarCategoria(Long id, String nombre, String descripcion) {
        Categoria categoria = categoriaRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada: " + id));
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
        return categoriaRepository.guardar(categoria);
    }

    @Override
    public void eliminarCategoria(Long id) {
        categoriaRepository.eliminarPorId(id);
    }

    @Override
    public Plato crearPlato(Long categoriaId, String nombre, String descripcion, BigDecimal precio) {
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (categoriaRepository.buscarPorId(categoriaId).isEmpty()) {
            throw new IllegalArgumentException("Categoría no existe: " + categoriaId);
        }
        Plato plato = new Plato(categoriaId, nombre, precio);
        plato.setDescripcion(descripcion);
        return platoRepository.guardar(plato);
    }

    @Override
    public List<Plato> listarPlatos() {
        return platoRepository.listarTodos();
    }

    @Override
    public List<Plato> listarPlatosPorCategoria(Long categoriaId) {
        return platoRepository.listarPorCategoria(categoriaId);
    }

    @Override
    public Optional<Plato> buscarPlatoPorId(Long id) {
        return platoRepository.buscarPorId(id);
    }

    @Override
    public Plato actualizarPlato(Long id, Long categoriaId, String nombre, BigDecimal precio) {
        Plato plato = platoRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado: " + id));
        plato.setCategoriaId(categoriaId);
        plato.setNombre(nombre);
        plato.setPrecio(precio);
        return platoRepository.guardar(plato);
    }

    @Override
    public void cambiarDisponibilidadPlato(Long id, boolean disponible) {
        Plato plato = platoRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Plato no encontrado: " + id));
        plato.setDisponible(disponible);
        platoRepository.guardar(plato);
    }

    @Override
    public void eliminarPlato(Long id) {
        platoRepository.eliminarPorId(id);
    }
}
