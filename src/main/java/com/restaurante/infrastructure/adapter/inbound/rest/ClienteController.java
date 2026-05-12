package com.restaurante.infrastructure.adapter.inbound.rest;

import com.restaurante.domain.model.Cliente;
import com.restaurante.domain.port.input.GestionClientesUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final GestionClientesUseCase gestionClientes;

    public ClienteController(GestionClientesUseCase gestionClientes) {
        this.gestionClientes = gestionClientes;
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody ClienteRequest request) {
        Cliente cliente = gestionClientes.crearCliente(
            request.getNombre(), request.getTelefono(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(gestionClientes.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return gestionClientes.buscarClientePorId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
