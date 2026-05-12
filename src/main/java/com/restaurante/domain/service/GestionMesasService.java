package com.restaurante.domain.service;

import com.restaurante.domain.model.Mesa;
import com.restaurante.domain.port.input.GestionMesasUseCase;
import com.restaurante.domain.port.output.MesaRepository;

import java.util.List;
import java.util.Optional;

public class GestionMesasService implements GestionMesasUseCase {

    private final MesaRepository mesaRepository;

    public GestionMesasService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    @Override
    public Mesa crearMesa(int numero, int capacidad, String ubicacion) {
        if (mesaRepository.buscarPorNumero(numero).isPresent()) {
            throw new IllegalArgumentException("Ya existe una mesa con el número: " + numero);
        }
        Mesa mesa = new Mesa();
        mesa.setNumero(numero);
        mesa.setCapacidad(capacidad);
        mesa.setUbicacion(ubicacion);
        mesa.setEstado("libre");
        return mesaRepository.guardar(mesa);
    }

    @Override
    public List<Mesa> listarMesas() {
        return mesaRepository.listarTodos();
    }

    @Override
    public List<Mesa> listarMesasLibres() {
        return mesaRepository.listarPorEstado("libre");
    }

    @Override
    public Optional<Mesa> buscarMesaPorId(Long id) {
        return mesaRepository.buscarPorId(id);
    }

    @Override
    public Optional<Mesa> buscarMesaPorNumero(int numero) {
        return mesaRepository.buscarPorNumero(numero);
    }

    @Override
    public void ocuparMesa(Long id) {
        Mesa mesa = mesaRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada: " + id));
        mesa.ocupar();
        mesaRepository.guardar(mesa);
    }

    @Override
    public void liberarMesa(Long id) {
        Mesa mesa = mesaRepository.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Mesa no encontrada: " + id));
        mesa.liberar();
        mesaRepository.guardar(mesa);
    }

    @Override
    public void eliminarMesa(Long id) {
        mesaRepository.eliminarPorId(id);
    }
}
