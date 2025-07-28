package com.marketplace.marketplace.service;

import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean registrar(UsuarioDTO dto) {
        // Verificar si el correo ya está registrado
        if (usuarioRepo.findByCorreo(dto.getCorreo()).isPresent()) {
            return false;
        }

        Usuario nuevo = new Usuario();
        nuevo.setNombre(dto.getNombre());
        nuevo.setCorreo(dto.getCorreo());

        // ✅ Encriptar la contraseña
        nuevo.setContrasena(passwordEncoder.encode(dto.getContrasena()));

        // Asignar un rol por defecto
        nuevo.setRol(dto.getRol()); // ✅ Ahora toma el rol desde el DTO correctamente
        usuarioRepo.save(nuevo);
        return true;
    }
}
