package com.restaurante.infrastructure.adapter.inbound.rest;

import com.restaurante.domain.model.Usuario;
import com.restaurante.domain.port.input.AutenticacionUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AutenticacionUseCase autenticacion;

    public AuthController(AutenticacionUseCase autenticacion) {
        this.autenticacion = autenticacion;
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@RequestBody LoginRequest request) {
        return autenticacion.login(request.getUsuario(), request.getContrasena())
            .map(u -> ResponseEntity.ok(toResponse(u)))
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> registrar(@RequestBody RegisterRequest request) {
        Usuario usuario = autenticacion.registrarUsuario(
            request.getNombre(), request.getRol(), request.getUsuario(), request.getContrasena());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(usuario));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<UsuarioResponse> usuarios = autenticacion.listarUsuarios().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNombre(), u.getRol(), u.getUsuario());
    }
}
