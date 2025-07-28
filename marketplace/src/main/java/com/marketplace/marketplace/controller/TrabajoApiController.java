package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.model.Trabajo;
import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.repository.TrabajoRepository;
import com.marketplace.marketplace.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trabajos")
public class TrabajoApiController {

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> registrarTrabajo(@RequestBody Trabajo trabajo, Authentication auth) {
        String correo = auth.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

        if (usuario != null) {
            trabajo.setUsuario(usuario);
            trabajoRepository.save(trabajo);
            return ResponseEntity.ok("Trabajo registrado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no autenticado");
        }
    }
}
