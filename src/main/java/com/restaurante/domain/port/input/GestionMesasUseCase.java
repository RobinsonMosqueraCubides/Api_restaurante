package com.restaurante.domain.port.input;

import com.restaurante.domain.model.Mesa;
import java.util.List;
import java.util.Optional;

public interface GestionMesasUseCase {
    Mesa crearMesa(int numero, int capacidad, String ubicacion);
    List<Mesa> listarMesas();
    List<Mesa> listarMesasLibres();
    Optional<Mesa> buscarMesaPorId(Long id);
    Optional<Mesa> buscarMesaPorNumero(int numero);
    void ocuparMesa(Long id);
    void liberarMesa(Long id);
    void eliminarMesa(Long id);
}
