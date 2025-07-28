package com.marketplace.marketplace.service;

import com.marketplace.marketplace.model.Rol;

public class UsuarioDTO {
    private String nombre;
    private String correo;
    private String contrasena;
    private Rol rol;  // ✅ cambia de String a Rol

    // Getters
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }
    public Rol getRol() { return rol; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setRol(Rol rol) { this.rol = rol; } // ✅ ahora funcional
}
