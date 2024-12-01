package com.app.ecomisiones.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Sucursal.SucursalServiceImpl;

@Controller
@RequestMapping("/direcciones")
public class DireccionController {

    @Autowired
    private CategoriaServiceImpl categoriaService;

    // Método para mostrar el carrito de compras
    @GetMapping("/ver")
    public String ver(Model model, Authentication auth) {
        try {

            Usuario usuario = (Usuario) auth.getPrincipal();
            List<Categoria> categorias = categoriaService.obtenerTodo();

            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("categorias", categorias);
            model.addAttribute("direcciones", usuario.getDirecciones());

            return "perfil/direcciones/verDirecciones";

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return "inicio";

        }
    }


    // Método para mostrar el carrito de compras
    @GetMapping("/nuevaDireccion")
    public String ver(Model model, Authentication auth) {
        try {

            Usuario usuario = (Usuario) auth.getPrincipal();
            List<Categoria> categorias = categoriaService.obtenerTodo();

            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("categorias", categorias);
            model.addAttribute("direcciones", usuario.getDirecciones());

            return "perfil/direcciones/nuevaDireccion";

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return "inicio";

        }
    }
    

}
