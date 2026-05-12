package com.restaurante.infrastructure.adapter.inbound.rest;

public class CrearPedidoRequest {
    private Long mesaId;
    private Long clienteId;
    private Long usuarioId;

    public Long getMesaId() { return mesaId; }
    public void setMesaId(Long mesaId) { this.mesaId = mesaId; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
