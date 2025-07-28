package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.model.Trabajo;
import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.repository.TrabajoRepository;
import com.marketplace.marketplace.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PerfilController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TrabajoRepository trabajoRepository;

@GetMapping("/perfil")
public String verPerfil(Model model, Authentication auth) {
    if (auth != null && auth.isAuthenticated()) {
        String correo = auth.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

        if (usuario != null) {
            model.addAttribute("usuario", usuario);

            // ⚠️ Agrega esto para que funcione el HTML
            List<Trabajo> trabajos = trabajoRepository.findByUsuario(usuario);
            model.addAttribute("trabajosUsuario", trabajos); // <--- ESTA LÍNEA ES CLAVE

            return "perfil";
        }
    }
    return "redirect:/auth/login";
}


}
