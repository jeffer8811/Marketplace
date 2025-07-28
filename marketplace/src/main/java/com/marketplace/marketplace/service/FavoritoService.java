package com.marketplace.marketplace.service;

import com.marketplace.marketplace.model.Favorito;
import com.marketplace.marketplace.model.Trabajo;
import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.repository.FavoritoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Transactional
    public void eliminarFavorito(Usuario usuario, Trabajo trabajo) {
        favoritoRepository.deleteByUsuarioAndTrabajo(usuario, trabajo);
    }
}
