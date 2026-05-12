package com.restaurante.domain.model;

import java.math.BigDecimal;

public class Plato {
    private Long id;
    private Long categoriaId;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private boolean disponible = true;

    public Plato() {}

    public Plato(Long categoriaId, String nombre, BigDecimal precio) {
        this.categoriaId = categoriaId;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = true;
    }

    public void marcarNoDisponible() {
        this.disponible = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
