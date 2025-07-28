package com.marketplace.marketplace.model;

import jakarta.persistence.*;

@Entity
public class Trabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private String categoria;

    @Column(name = "imagen_url")
    private String imagenUrl;

    private Double precio;
    private String numeroContacto;

    @Column(nullable = false)
    private boolean aprobado = false; // Por defecto no aprobado

    @ManyToOne
    private Usuario usuario;

    // Constructor vacío obligatorio para JPA
    public Trabajo() {}

    // Constructor con parámetros
    public Trabajo(String titulo, String descripcion, String categoria, String imagenUrl,
                   Double precio, String numeroContacto) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
        this.precio = precio;
        this.numeroContacto = numeroContacto;
        this.aprobado = false;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getNumeroContacto() {
        return numeroContacto;
    }

    public void setNumeroContacto(String numeroContacto) {
        this.numeroContacto = numeroContacto;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
