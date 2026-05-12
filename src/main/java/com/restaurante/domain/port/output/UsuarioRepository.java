package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorUsuario(String usuario);
    List<Usuario> listarTodos();
}
