package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.model.Mensaje;
import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.repository.MensajeRepository;
import com.marketplace.marketplace.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class ChatController {

    @Autowired
    private MensajeRepository mensajeRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping("/chat/{usuarioId}")
    public String verChat(@PathVariable Long usuarioId, Model model, Principal principal) {
        Usuario actual = usuarioRepo.findByCorreo(principal.getName()).orElse(null);
        Usuario otro = usuarioRepo.findById(usuarioId).orElse(null);

        if (actual == null || otro == null) {
            return "redirect:/";
        }

        List<Mensaje> mensajes = mensajeRepo.obtenerConversacion(actual.getId(), otro.getId());

        model.addAttribute("mensajes", mensajes);
        model.addAttribute("otroUsuario", otro);
        model.addAttribute("actual", actual);
        return "chat";
    }

    // 🟢 Enviar un nuevo mensaje
    @PostMapping("/chat/enviar")
    public String enviarMensaje(@RequestParam Long receptorId,
            @RequestParam String contenido,
            Principal principal) {
        Optional<Usuario> emisorOpt = usuarioRepo.findByCorreo(principal.getName());
        Optional<Usuario> receptorOpt = usuarioRepo.findById(receptorId);

        if (emisorOpt.isPresent() && receptorOpt.isPresent() && !contenido.isBlank()) {
            Mensaje mensaje = new Mensaje();
            mensaje.setEmisor(emisorOpt.get());
            mensaje.setReceptor(receptorOpt.get());
            mensaje.setContenido(contenido);
            mensaje.setFechaEnvio(LocalDateTime.now());

            mensajeRepo.save(mensaje);
        }

        return "redirect:/chat/" + receptorId;
    }

    // 🟢 Mostrar lista de conversaciones (inbox)
    @GetMapping("/chat/inbox")
    public String verInbox(Model model, Principal principal) {
        Usuario actual = usuarioRepo.findByCorreo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Mensaje> enviados = mensajeRepo.findByEmisorId(actual.getId());
        List<Mensaje> recibidos = mensajeRepo.findByReceptorId(actual.getId());

        Set<Usuario> conversados = new HashSet<>();
        for (Mensaje m : enviados) {
            conversados.add(m.getReceptor());
        }
        for (Mensaje m : recibidos) {
            conversados.add(m.getEmisor());
        }

        model.addAttribute("usuariosConversados", conversados);
        return "inbox"; // nombre del HTML para listar chats
    }
}
