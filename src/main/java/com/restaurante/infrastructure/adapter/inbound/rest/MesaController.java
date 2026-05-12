package com.restaurante.infrastructure.adapter.inbound.rest;

import com.restaurante.domain.model.Mesa;
import com.restaurante.domain.port.input.GestionMesasUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final GestionMesasUseCase gestionMesas;

    public MesaController(GestionMesasUseCase gestionMesas) {
        this.gestionMesas = gestionMesas;
    }

    @PostMapping
    public ResponseEntity<Mesa> crear(@RequestBody CrearMesaRequest request) {
        Mesa mesa = gestionMesas.crearMesa(request.getNumero(), request.getCapacidad(), request.getUbicacion());
        return ResponseEntity.status(HttpStatus.CREATED).body(mesa);
    }

    @GetMapping
    public ResponseEntity<List<Mesa>> listar() {
        return ResponseEntity.ok(gestionMesas.listarMesas());
    }

    @GetMapping("/libres")
    public ResponseEntity<List<Mesa>> listarLibres() {
        return ResponseEntity.ok(gestionMesas.listarMesasLibres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mesa> buscar(@PathVariable Long id) {
        return gestionMesas.buscarMesaPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/ocupar")
    public ResponseEntity<Void> ocupar(@PathVariable Long id) {
        gestionMesas.ocuparMesa(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/liberar")
    public ResponseEntity<Void> liberar(@PathVariable Long id) {
        gestionMesas.liberarMesa(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gestionMesas.eliminarMesa(id);
        return ResponseEntity.noContent().build();
    }
}
