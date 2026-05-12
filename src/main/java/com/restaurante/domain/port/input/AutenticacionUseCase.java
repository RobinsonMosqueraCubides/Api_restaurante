package com.restaurante.domain.port.input;

import com.restaurante.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface AutenticacionUseCase {
    Optional<Usuario> login(String usuario, String contrasena);
    Usuario registrarUsuario(String nombre, String rol, String usuario, String contrasena);
    List<Usuario> listarUsuarios();
}
