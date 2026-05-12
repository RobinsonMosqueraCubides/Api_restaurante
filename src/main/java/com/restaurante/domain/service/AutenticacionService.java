package com.restaurante.domain.service;

import com.restaurante.domain.model.Usuario;
import com.restaurante.domain.port.input.AutenticacionUseCase;
import com.restaurante.domain.port.output.UsuarioRepository;

import java.util.List;
import java.util.Optional;

public class AutenticacionService implements AutenticacionUseCase {

    private final UsuarioRepository usuarioRepository;

    public AutenticacionService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<Usuario> login(String usuario, String contrasena) {
        return usuarioRepository.buscarPorUsuario(usuario)
            .filter(u -> verificarContrasena(contrasena, u.getContrasenaHash()));
    }

    @Override
    public Usuario registrarUsuario(String nombre, String rol, String usuario, String contrasena) {
        if (usuarioRepository.buscarPorUsuario(usuario).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe: " + usuario);
        }
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setRol(rol);
        u.setUsuario(usuario);
        u.setContrasenaHash(hashContrasena(contrasena));
        return usuarioRepository.guardar(u);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }

    private boolean verificarContrasena(String raw, String hash) {
        return raw.equals(hash); // TODO: usar BCrypt
    }

    private String hashContrasena(String contrasena) {
        return contrasena; // TODO: usar BCrypt
    }
}
