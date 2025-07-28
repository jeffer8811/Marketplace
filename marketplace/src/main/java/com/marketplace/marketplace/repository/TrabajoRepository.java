package com.marketplace.marketplace.repository;

import com.marketplace.marketplace.model.Trabajo;
import com.marketplace.marketplace.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrabajoRepository extends JpaRepository<Trabajo, Long> {

    // Buscar por categoría (sin importar si está aprobado o no)
    List<Trabajo> findByCategoria(String categoria);

    // Buscar solo trabajos aprobados (para mostrar al público)
    List<Trabajo> findByAprobadoTrue();

    // Buscar por categoría y aprobados
    List<Trabajo> findByCategoriaAndAprobadoTrue(String categoria);

    // Buscar todos los trabajos de un usuario
    List<Trabajo> findByUsuario(Usuario usuario);

    // Buscar solo trabajos aprobados de un usuario
    List<Trabajo> findByUsuarioAndAprobadoTrue(Usuario usuario);

    // Alternativa por ID de usuario
    List<Trabajo> findByUsuarioId(Long usuarioId);

    // Buscar trabajos por estado de aprobación
    List<Trabajo> findByAprobado(boolean estado);

    // Paginación: por estado
    Page<Trabajo> findByAprobado(boolean aprobado, Pageable pageable);

    // Paginación: todos los trabajos
    Page<Trabajo> findAll(Pageable pageable);
}
