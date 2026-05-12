package com.restaurante.domain.service;

import com.restaurante.domain.model.Usuario;
import com.restaurante.domain.port.input.AutenticacionUseCase;
import com.restaurante.domain.port.output.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

public class AutenticacionService implements AutenticacionUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AutenticacionService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Usuario> login(String usuario, String contrasena) {
        return usuarioRepository.buscarPorUsuario(usuario)
            .filter(u -> passwordEncoder.matches(contrasena, u.getContrasenaHash()));
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
        u.setContrasenaHash(passwordEncoder.encode(contrasena));
        return usuarioRepository.guardar(u);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.listarTodos();
    }
}
