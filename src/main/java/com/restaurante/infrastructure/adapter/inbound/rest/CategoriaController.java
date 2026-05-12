package com.restaurante.infrastructure.adapter.inbound.rest;

import com.restaurante.domain.model.Categoria;
import com.restaurante.domain.port.input.GestionMenuUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final GestionMenuUseCase gestionMenu;

    public CategoriaController(GestionMenuUseCase gestionMenu) {
        this.gestionMenu = gestionMenu;
    }

    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody CrearCategoriaRequest request) {
        Categoria categoria = gestionMenu.crearCategoria(request.getNombre(), request.getDescripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(gestionMenu.listarCategorias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscar(@PathVariable Long id) {
        return gestionMenu.buscarCategoriaPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @RequestBody CrearCategoriaRequest request) {
        Categoria categoria = gestionMenu.actualizarCategoria(id, request.getNombre(), request.getDescripcion());
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gestionMenu.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
