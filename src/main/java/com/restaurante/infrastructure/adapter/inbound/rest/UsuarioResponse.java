package com.restaurante.infrastructure.adapter.inbound.rest;

public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String rol;
    private String usuario;

    public UsuarioResponse() {}

    public UsuarioResponse(Long id, String nombre, String rol, String usuario) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.usuario = usuario;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
}
