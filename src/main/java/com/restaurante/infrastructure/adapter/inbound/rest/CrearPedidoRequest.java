package com.restaurante.infrastructure.adapter.inbound.rest;

import jakarta.validation.constraints.NotNull;

public class CrearPedidoRequest {

    @NotNull(message = "El ID de mesa es obligatorio")
    private Long mesaId;

    private Long clienteId;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    public Long getMesaId() { return mesaId; }
    public void setMesaId(Long mesaId) { this.mesaId = mesaId; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
