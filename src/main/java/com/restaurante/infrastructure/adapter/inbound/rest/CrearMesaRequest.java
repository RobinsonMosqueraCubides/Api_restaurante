package com.restaurante.infrastructure.adapter.inbound.rest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CrearMesaRequest {

    @NotNull(message = "El numero de mesa es obligatorio")
    @Min(value = 1, message = "El numero debe ser mayor a 0")
    private int numero;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private int capacidad;

    private String ubicacion;

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}
