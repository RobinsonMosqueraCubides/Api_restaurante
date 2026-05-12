package com.restaurante.infrastructure.adapter.inbound.rest;

import com.restaurante.domain.model.Usuario;
import com.restaurante.domain.port.input.AutenticacionUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AutenticacionUseCase autenticacion;

    public AuthController(AutenticacionUseCase autenticacion) {
        this.autenticacion = autenticacion;
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest request) {
        return autenticacion.login(request.getUsuario(), request.getContrasena())
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> registrar(@RequestBody RegisterRequest request) {
        Usuario usuario = autenticacion.registrarUsuario(
            request.getNombre(), request.getRol(), request.getUsuario(), request.getContrasena());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(autenticacion.listarUsuarios());
    }
}
