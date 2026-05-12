package com.restaurante.domain.port.output;

import com.restaurante.domain.model.Mesa;
import java.util.List;
import java.util.Optional;

public interface MesaRepository {
    Mesa guardar(Mesa mesa);
    Optional<Mesa> buscarPorId(Long id);
    Optional<Mesa> buscarPorNumero(int numero);
    List<Mesa> listarTodos();
    List<Mesa> listarPorEstado(String estado);
    void eliminarPorId(Long id);
}
