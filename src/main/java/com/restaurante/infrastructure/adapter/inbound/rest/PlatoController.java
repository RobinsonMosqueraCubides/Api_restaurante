package com.restaurante.infrastructure.adapter.inbound.rest;

import com.restaurante.domain.model.Plato;
import com.restaurante.domain.port.input.GestionMenuUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platos")
public class PlatoController {

    private final GestionMenuUseCase gestionMenu;

    public PlatoController(GestionMenuUseCase gestionMenu) {
        this.gestionMenu = gestionMenu;
    }

    @PostMapping
    public ResponseEntity<Plato> crear(@RequestBody CrearPlatoRequest request) {
        Plato plato = gestionMenu.crearPlato(
            request.getCategoriaId(), request.getNombre(),
            request.getDescripcion(), request.getPrecio());
        return ResponseEntity.status(HttpStatus.CREATED).body(plato);
    }

    @GetMapping
    public ResponseEntity<List<Plato>> listar(@RequestParam(required = false) Long categoriaId) {
        if (categoriaId != null) {
            return ResponseEntity.ok(gestionMenu.listarPlatosPorCategoria(categoriaId));
        }
        return ResponseEntity.ok(gestionMenu.listarPlatos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plato> buscar(@PathVariable Long id) {
        return gestionMenu.buscarPlatoPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plato> actualizar(@PathVariable Long id, @RequestBody CrearPlatoRequest request) {
        Plato plato = gestionMenu.actualizarPlato(
            id, request.getCategoriaId(), request.getNombre(), request.getPrecio());
        return ResponseEntity.ok(plato);
    }

    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<Void> cambiarDisponibilidad(@PathVariable Long id, @RequestParam boolean disponible) {
        gestionMenu.cambiarDisponibilidadPlato(id, disponible);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gestionMenu.eliminarPlato(id);
        return ResponseEntity.noContent().build();
    }
}
