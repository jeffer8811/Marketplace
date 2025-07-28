package com.marketplace.marketplace.repository;

import com.marketplace.marketplace.model.Favorito;
import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.model.Trabajo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByUsuario(Usuario usuario);
    Optional<Favorito> findByUsuarioAndTrabajo(Usuario usuario, Trabajo trabajo);
    void deleteByUsuarioAndTrabajo(Usuario usuario, Trabajo trabajo);
}
