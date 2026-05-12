package com.restaurante.infrastructure.adapter.inbound.rest;

import com.restaurante.domain.model.Factura;
import com.restaurante.domain.port.input.FacturacionUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturacionUseCase facturacion;

    public FacturaController(FacturacionUseCase facturacion) {
        this.facturacion = facturacion;
    }

    @PostMapping("/pedido/{pedidoId}")
    public ResponseEntity<Factura> generar(@PathVariable Long pedidoId, @RequestParam String metodoPago) {
        Factura factura = facturacion.generarFactura(pedidoId, metodoPago);
        return ResponseEntity.status(HttpStatus.CREATED).body(factura);
    }

    @GetMapping
    public ResponseEntity<List<Factura>> listar() {
        return ResponseEntity.ok(facturacion.listarFacturas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> buscar(@PathVariable Long id) {
        return facturacion.buscarFacturaPorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Factura> buscarPorPedido(@PathVariable Long pedidoId) {
        return facturacion.buscarFacturaPorPedido(pedidoId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
