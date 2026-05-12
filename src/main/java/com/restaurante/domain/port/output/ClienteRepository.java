package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    List<Cliente> listarTodos();
    Optional<Cliente> buscarPorTelefono(String telefono);
}
