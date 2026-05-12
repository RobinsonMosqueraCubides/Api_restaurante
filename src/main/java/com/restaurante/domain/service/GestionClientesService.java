package com.restaurante.domain.service;

import com.restaurante.domain.model.Cliente;
import com.restaurante.domain.port.input.GestionClientesUseCase;
import com.restaurante.domain.port.output.ClienteRepository;

import java.util.List;
import java.util.Optional;

public class GestionClientesService implements GestionClientesUseCase {

    private final ClienteRepository clienteRepository;

    public GestionClientesService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente crearCliente(String nombre, String telefono, String email) {
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        return clienteRepository.guardar(cliente);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.listarTodos();
    }

    @Override
    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.buscarPorId(id);
    }
}
