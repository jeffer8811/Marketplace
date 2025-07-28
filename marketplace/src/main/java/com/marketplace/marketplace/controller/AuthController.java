package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.model.Rol;
import com.marketplace.marketplace.service.UsuarioDTO;
import com.marketplace.marketplace.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin(Authentication authentication) {
        System.out.println("🧠 Authentication: " + authentication);

        if (authentication != null && authentication.isAuthenticated()) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            return isAdmin ? "redirect:/admin/trabajo" : "redirect:/index";
        }

        return "login"; // Página de login si no está autenticado
    }

    @GetMapping("/register")
    public String mostrarRegistro(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            return isAdmin ? "redirect:/admin/trabajo" : "redirect:/index";
        }
        return "register";
    }

    @PostMapping("/register")
    public String procesarRegistro(@RequestParam String nombre,
            @RequestParam String correo,
            @RequestParam String contrasena,
            Model model) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre(nombre);
        dto.setCorreo(correo);
        dto.setContrasena(contrasena);

        // ✅ Añadido: asignar rol según correo
        if (correo.toLowerCase().endsWith(".edu.pe")) {
            dto.setRol(Rol.ESTUDIANTE);
        } else {
            dto.setRol(Rol.COMPRADOR);
        }

        boolean registrado = usuarioService.registrar(dto);
        if (registrado) {
            return "redirect:/auth/login";
        } else {
            model.addAttribute("error", "El correo ya está registrado");
            return "register";
        }
    }
}
