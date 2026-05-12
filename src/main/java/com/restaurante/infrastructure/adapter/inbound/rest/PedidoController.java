package com.restaurante.infrastructure.adapter.inbound.rest;

import com.restaurante.domain.model.Pedido;
import com.restaurante.domain.port.input.TomarPedidoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final TomarPedidoUseCase tomarPedido;

    public PedidoController(TomarPedidoUseCase tomarPedido) {
        this.tomarPedido = tomarPedido;
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@Valid @RequestBody CrearPedidoRequest request) {
        Pedido pedido = tomarPedido.crearPedido(
            request.getMesaId(), request.getClienteId(), request.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PostMapping("/{id}/platos")
    public ResponseEntity<Void> agregarPlato(@PathVariable Long id, @Valid @RequestBody AgregarPlatoRequest request) {
        tomarPedido.agregarPlatoAPedido(id, request.getPlatoId(), request.getCantidad());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Pedido>> listarActivos() {
        return ResponseEntity.ok(tomarPedido.listarPedidosActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return tomarPedido.buscarPedidoPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/preparacion")
    public ResponseEntity<Void> marcarPreparacion(@PathVariable Long id) {
        tomarPedido.marcarEnPreparacion(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/entregar")
    public ResponseEntity<Void> marcarEntregado(@PathVariable Long id) {
        tomarPedido.marcarEntregado(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        tomarPedido.cancelarPedido(id);
        return ResponseEntity.ok().build();
    }
}
