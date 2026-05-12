package com.restaurante.infrastructure.adapter.inbound.rest;

public class CrearMesaRequest {
    private int numero;
    private int capacidad;
    private String ubicacion;

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}
