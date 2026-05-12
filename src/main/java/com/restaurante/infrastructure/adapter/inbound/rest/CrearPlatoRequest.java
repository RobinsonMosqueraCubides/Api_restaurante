package com.restaurante.infrastructure.adapter.inbound.rest;

import java.math.BigDecimal;

public class CrearPlatoRequest {
    private Long categoriaId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
}
