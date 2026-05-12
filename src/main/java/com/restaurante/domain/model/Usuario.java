package com.restaurante.domain.model;

public class Usuario {
    private Long id;
    private String nombre;
    private String rol;
    private String usuario;
    private String contrasenaHash;

    public Usuario() {}

    public boolean esAdmin() {
        return "admin".equals(this.rol);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }
}
