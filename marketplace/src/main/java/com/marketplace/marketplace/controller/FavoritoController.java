package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.model.Favorito;
import com.marketplace.marketplace.model.Trabajo;
import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.repository.FavoritoRepository;
import com.marketplace.marketplace.repository.TrabajoRepository;
import com.marketplace.marketplace.service.UsuarioAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private TrabajoRepository trabajoRepository;

    @Autowired
    private UsuarioAppService usuarioService;

    // ✅ Agregar a favoritos
    @PostMapping("/agregar/{id}")
    public String agregarAFavoritos(@PathVariable Long id, Principal principal) {
        Usuario usuario = usuarioService.obtenerPorCorreo(principal.getName()).orElseThrow();
        Trabajo trabajo = trabajoRepository.findById(id).orElseThrow();

        favoritoRepository.findByUsuarioAndTrabajo(usuario, trabajo)
            .orElseGet(() -> {
                Favorito nuevo = new Favorito();
                nuevo.setUsuario(usuario);
                nuevo.setTrabajo(trabajo);
                return favoritoRepository.save(nuevo);
            });

        return "redirect:/productos"; // o desde donde se esté llamando
    }

@org.springframework.transaction.annotation.Transactional
@PostMapping("/eliminar/{id}")
public String eliminarDeFavoritos(@PathVariable Long id, Principal principal) {
    Usuario usuario = usuarioService.obtenerPorCorreo(principal.getName()).orElseThrow();
    Trabajo trabajo = trabajoRepository.findById(id).orElseThrow();

    favoritoRepository.deleteByUsuarioAndTrabajo(usuario, trabajo);

    return "redirect:/favoritos";
}

    // ✅ Ver lista de favoritos
    @GetMapping
    public String verFavoritos(Model model, Principal principal) {
        Usuario usuario = usuarioService.obtenerPorCorreo(principal.getName()).orElseThrow();
        List<Favorito> favoritos = favoritoRepository.findByUsuario(usuario);
        model.addAttribute("favoritos", favoritos);
        return "favoritos";
    }
}
