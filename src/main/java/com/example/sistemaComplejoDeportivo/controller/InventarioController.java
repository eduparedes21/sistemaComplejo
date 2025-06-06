package com.example.sistemaComplejoDeportivo.controller;

import com.example.sistemaComplejoDeportivo.model.Inventario;
import com.example.sistemaComplejoDeportivo.model.Proveedor;
import com.example.sistemaComplejoDeportivo.service.InventarioService;
import com.example.sistemaComplejoDeportivo.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private UsuarioController usuarioController;
    
    // 📌 Mostrar el inventario
    @GetMapping
    public String mostrarInventario(Model model) {
        List<Inventario> productos = inventarioService.listarTodosLosArticulos();
        model.addAttribute("productos", productos);
        return "inventario"; // Asegúrate de que inventario.html esté en templates/
    }

    // 📌 Cargar formulario de agregar producto
    @GetMapping("/nuevo")
    public String formularioNuevoProducto(Model model) {
        List<Proveedor> proveedores = proveedorService.obtenerTodosLosProveedores();
        model.addAttribute("producto", new Inventario());
        model.addAttribute("proveedores", proveedores); // Agregar proveedores a la vista
        return "crear-producto"; // Asegúrate de que crear-producto.html esté en templates/
    }

    // 📌 Guardar un nuevo producto
    @PostMapping("/crear")
    public String crearProducto(@ModelAttribute Inventario producto, RedirectAttributes redirectAttributes) {
        try {
            inventarioService.crearArticulo(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar el producto: " + e.getMessage());
        }
        return "redirect:/inventario";
    }

    // 📌 Cargar formulario de edición
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        //Se verifica si el usuario no es Administrador
        if (!usuarioController.esAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar el producto.");
            return "redirect:/inventario";
        }
        else{
        Optional<Inventario> producto = inventarioService.obtenerArticuloPorId(id);
        if (producto.isPresent()) {
            List<Proveedor> proveedores = proveedorService.obtenerTodosLosProveedores();
            model.addAttribute("producto", producto.get());
            model.addAttribute("proveedores", proveedores); // Añadir proveedores al modelo
            return "editar-inventario"; // Asegúrate de crear esta vista
        } else {
            redirectAttributes.addFlashAttribute("error", "El producto no existe.");
            return "redirect:/inventario";
        }
        }
    }

    // 📌 Actualizar producto
    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Integer id, @ModelAttribute Inventario producto, RedirectAttributes redirectAttributes) {
        //Se verifica si el usuario no es Administrador
        if (!usuarioController.esAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para actualizar el producto.");
            return "redirect:/inventario";
        }
        else {
        try {
            inventarioService.actualizarArticulo(id, producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el producto.");
        }
        }
        return "redirect:/inventario";
    }

    // 📌 Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        //Se verifica si el usuario no es Administrador
        if (!usuarioController.esAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar el producto.");
            return "redirect:/inventario";
        }
        else{
        try {
            inventarioService.eliminarArticulo(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar el producto.");
        }
        }
        return "redirect:/inventario";
    }
}
