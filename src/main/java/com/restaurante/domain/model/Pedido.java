package com.restaurante.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Long id;
    private Long mesaId;
    private Long clienteId;
    private Long usuarioId;
    private LocalDateTime fecha = LocalDateTime.now();
    private String estado = "activo";
    private BigDecimal total = BigDecimal.ZERO;
    private List<DetallePedido> detalle = new ArrayList<>();

    public Pedido() {}

    public void agregarDetalle(DetallePedido item) {
        this.detalle.add(item);
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.total = detalle.stream()
            .map(DetallePedido::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void cerrar() {
        this.estado = "pagado";
    }

    public void cancelar() {
        this.estado = "cancelado";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMesaId() { return mesaId; }
    public void setMesaId(Long mesaId) { this.mesaId = mesaId; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public List<DetallePedido> getDetalle() { return detalle; }
    public void setDetalle(List<DetallePedido> detalle) { this.detalle = detalle; }
}
