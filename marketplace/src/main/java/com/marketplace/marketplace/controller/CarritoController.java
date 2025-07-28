package com.marketplace.marketplace.controller;

import com.marketplace.marketplace.model.ItemCarrito;
import com.marketplace.marketplace.model.Pedido;
import com.marketplace.marketplace.model.Usuario;
import com.marketplace.marketplace.repository.PedidoRepository;
import com.marketplace.marketplace.service.UsuarioAppService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioAppService usuarioService;

    // Mostrar carrito
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        model.addAttribute("carrito", carrito);

        double total = carrito.stream()
                .mapToDouble(i -> i.getPrecio() * i.getCantidad())
                .sum();
        model.addAttribute("total", total);

        return "carrito";
    }

    // Agregar producto al carrito
    @PostMapping("/agregar")
    public String agregarProducto(@RequestParam Long id,
                                  @RequestParam String nombre,
                                  @RequestParam double precio,
                                  @RequestParam int cantidad,
                                  HttpSession session) {

        List<ItemCarrito> carrito = obtenerCarrito(session);

        carrito.stream()
                .filter(p -> p.getProductoId().equals(id))
                .findFirst()
                .ifPresentOrElse(
                        existente -> existente.setCantidad(existente.getCantidad() + cantidad),
                        () -> carrito.add(new ItemCarrito(id, nombre, precio, cantidad))
                );

        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }

    // Eliminar producto del carrito
    @PostMapping("/eliminar")
    public String eliminarProducto(@RequestParam Long id, HttpSession session) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        carrito.removeIf(p -> p.getProductoId().equals(id));
        session.setAttribute("carrito", carrito);
        return "redirect:/carrito";
    }

    // Mostrar formulario de pago
    @GetMapping("/pago")
    public String mostrarFormularioPago(HttpSession session) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        if (carrito.isEmpty()) {
            return "redirect:/carrito";
        }
        return "pago";
    }

    // Procesar pedido (simulación de pago)
    @PostMapping("/procesar")
    public String procesarPago(HttpSession session, Principal principal) {
        List<ItemCarrito> carrito = obtenerCarrito(session);
        if (carrito.isEmpty()) {
            return "redirect:/carrito";
        }

        double total = carrito.stream()
                .mapToDouble(i -> i.getPrecio() * i.getCantidad())
                .sum();

        Usuario usuario = usuarioService.obtenerPorCorreo(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setFecha(new Date());
        pedido.setTotal(total);
        pedido.setUsuario(usuario);

        pedidoRepository.save(pedido);
        session.removeAttribute("carrito");

        return "redirect:/confirmacion";
    }

    // Método de utilidad para obtener el carrito actual
    @SuppressWarnings("unchecked")
    private List<ItemCarrito> obtenerCarrito(HttpSession session) {
        Object obj = session.getAttribute("carrito");
        if (obj instanceof List<?>) {
            return (List<ItemCarrito>) obj;
        }
        return new ArrayList<>();
    }
}
