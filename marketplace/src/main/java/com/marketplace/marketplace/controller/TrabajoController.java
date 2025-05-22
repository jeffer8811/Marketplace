package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.model.Trabajo;
import com.marketplace.marketplace.repository.TrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Optional;

@Controller
public class TrabajoController {

    @Autowired
    private TrabajoRepository trabajoRepository;

    @GetMapping("/")
    public String mostrarTrabajos(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findAll();
        model.addAttribute("trabajos", trabajos);
        return "formulario";
    }

    @PostMapping("/registrar-trabajo")
    public String registrarTrabajo(@ModelAttribute Trabajo trabajo) {
        // Aquí el campo imagenUrl ya contiene la URL que el usuario escribió.
        trabajoRepository.save(trabajo);
        return "redirect:/";
    }

    @GetMapping("/diseno-grafico")
    public String mostrarDisenoGrafico(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findByCategoria("Diseño Gráfico");
        model.addAttribute("trabajos", trabajos);
        return "diseno-grafico";
    }

    @GetMapping("/index")
    public String mostrarIndex(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findByCategoria("Pagina Principal");
        model.addAttribute("trabajos", trabajos);
        return "index";
    }

    @GetMapping("/desarrollo-web")
    public String mostrarDesarrolloWeb(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findByCategoria("Desarrollo Web");
        model.addAttribute("trabajos", trabajos);
        return "desarrollo-web";
    }

    @GetMapping("/ilustraciones")
    public String mostrarIlustraciones(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findByCategoria("Ilustraciones");
        model.addAttribute("trabajos", trabajos);
        return "ilustraciones";
    }

    @Configuration
    public class MvcConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:uploads/");
        }
    }

    @GetMapping("/admin/trabajos")
    public String listarTrabajosAdmin(Model model) {
        List<Trabajo> trabajos = trabajoRepository.findAll();
        model.addAttribute("trabajos", trabajos);
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


    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";  // Esto buscará `login.html` en src/main/resources/templates si usas Thymeleaf
    }


}