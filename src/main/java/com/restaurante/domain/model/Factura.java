package com.restaurante.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Factura {
    private Long id;
    private Long pedidoId;
    private BigDecimal monto;
    private String metodoPago;
    private LocalDateTime fecha = LocalDateTime.now();

    public Factura() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
