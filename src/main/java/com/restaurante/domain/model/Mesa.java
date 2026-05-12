package com.restaurante.domain.model;

public class Mesa {
    private Long id;
    private int numero;
    private int capacidad;
    private String ubicacion;
    private String estado = "libre";

    public Mesa() {}

    public void ocupar() {
        this.estado = "ocupada";
    }

    public void liberar() {
        this.estado = "libre";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
