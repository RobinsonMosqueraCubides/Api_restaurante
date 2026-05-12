package com.restaurante.domain.port.input;

import com.restaurante.domain.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface GestionClientesUseCase {
    Cliente crearCliente(String nombre, String telefono, String email);
    List<Cliente> listarClientes();
    Optional<Cliente> buscarClientePorId(Long id);
}
