package com.restaurante.infrastructure.adapter.inbound.rest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AgregarPlatoRequest {

    @NotNull(message = "El ID del plato es obligatorio")
    private Long platoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    public Long getPlatoId() { return platoId; }
    public void setPlatoId(Long platoId) { this.platoId = platoId; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
