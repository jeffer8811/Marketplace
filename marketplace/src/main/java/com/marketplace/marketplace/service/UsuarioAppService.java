package com.marketplace.marketplace.service;

import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioAppService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioAppService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> obtenerPorCorreo(String correo) {
        if (correo == null || correo.isBlank()) {
            return Optional.empty(); // Evita errores si el correo viene nulo o vacío
        }
        return usuarioRepository.findByCorreo(correo);
    }
}
