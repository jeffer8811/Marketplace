package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.model.Trabajo;
import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.repository.TrabajoRepository;
import com.marketplace.marketplace.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Controller
public class TrabajoController {

    @Autowired
    private TrabajoRepository trabajoRepository;

    @GetMapping("/")
    public String mostrarIndex(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findByCategoria("Pagina Principal");
        model.addAttribute("trabajos", trabajos);
        return "index";
    }

    @GetMapping("/formulario")
    public String mostrarFormulario(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findAll();
        model.addAttribute("trabajos", trabajos);
        return "formulario";
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/registrar-trabajo")
    public String registrarTrabajo(@ModelAttribute Trabajo trabajo, Authentication auth) {
        String correo = auth.getName(); // Usuario autenticado
        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

        if (usuario != null) {
            trabajo.setUsuario(usuario); // ✅ Asignar el usuario antes de guardar
            trabajoRepository.save(trabajo); // ✅ Guardar con el usuario asignado
        }

        return "redirect:/perfil";
    }

    @GetMapping("/diseno-grafico")
    public String mostrarDisenoGrafico(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findByCategoriaAndAprobadoTrue("Diseño Gráfico");
        model.addAttribute("trabajos", trabajos);
        return "diseno-grafico";
    }

    @GetMapping("/desarrollo-web")
    public String mostrarDesarrolloWeb(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findByCategoriaAndAprobadoTrue("Desarrollo Web");
        model.addAttribute("trabajos", trabajos);
        return "desarrollo-web";
    }

    @GetMapping("/ilustraciones")
    public String mostrarIlustraciones(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findByCategoriaAndAprobadoTrue("Ilustraciones");
        model.addAttribute("trabajos", trabajos);
        return "ilustraciones";
    }

    @GetMapping("/productos")
    public String mostrarTodosLosProductos(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findByAprobadoTrue();
        model.addAttribute("trabajos", trabajos);
        return "productos";
    }

    @GetMapping("/admin/trabajos")
    public String listarTrabajosAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String aprobado,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Trabajo> trabajosPage;

        if (aprobado == null || aprobado.isEmpty()) {
            trabajosPage = trabajoRepository.findAll(pageable);
        } else {
            boolean estado = Boolean.parseBoolean(aprobado);
            trabajosPage = trabajoRepository.findByAprobado(estado, pageable);
        }

        model.addAttribute("trabajosPage", trabajosPage);
        model.addAttribute("trabajos", trabajosPage.getContent());
        model.addAttribute("aprobado", aprobado);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", trabajosPage.getTotalPages());

        return "admin-trabajos";
    }

    @GetMapping("/admin/trabajos/eliminar/{id}")
    public String eliminarTrabajo(@PathVariable Long id) {
        trabajoRepository.deleteById(id);
        return "redirect:/admin/trabajos";
    }

    @GetMapping("/admin/trabajos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Trabajo trabajo = trabajoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trabajo no encontrado: " + id));
        model.addAttribute("trabajo", trabajo);
        return "editar-trabajo";
    }

    @PostMapping("/admin/trabajos/editar/{id}")
    public String guardarCambios(
            @PathVariable Long id,
            @RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String categoria,
            @RequestParam double precio,
            @RequestParam String imagenUrl) {
        Trabajo trabajo = trabajoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trabajo no encontrado: " + id));

        trabajo.setTitulo(titulo);
        trabajo.setDescripcion(descripcion);
        trabajo.setCategoria(categoria);
        trabajo.setPrecio(precio);
        trabajo.setImagenUrl(imagenUrl); // ← Guardar la URL directamente

        trabajoRepository.save(trabajo);

        return "redirect:/admin/trabajos";
    }

    @GetMapping("/trabajo/{id}")
    public String verDetalleTrabajo(@PathVariable Long id, Model model) {
        Optional<Trabajo> trabajoOptional = trabajoRepository.findById(id);
        if (trabajoOptional.isPresent()) {
            model.addAttribute("trabajo", trabajoOptional.get());
            return "detalle-trabajo"; // este debe coincidir con el nombre del archivo .html
        } else {
            return "redirect:/"; // o podrías redirigir a una página de error
        }
    }

    @GetMapping("/admin/trabajos/aprobar/{id}")
    public String aprobarTrabajo(@PathVariable Long id) {
        Trabajo trabajo = trabajoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trabajo no encontrado"));
        trabajo.setAprobado(true);
        trabajoRepository.save(trabajo);
        return "redirect:/admin/trabajos";
    }

    @GetMapping("/admin/trabajos/desaprobar/{id}")
    public String desaprobarTrabajo(@PathVariable Long id) {
        Trabajo trabajo = trabajoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trabajo no encontrado"));
        trabajo.setAprobado(false);
        trabajoRepository.save(trabajo);
        return "redirect:/admin/trabajos";
    }

}