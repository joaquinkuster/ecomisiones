package com.app.ecomisiones.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.MedioDePago;
import com.app.ecomisiones.model.Compra;
import com.app.ecomisiones.model.DetalleCompra;
import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.model.Sucursal;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.Compra.CompraServiceImpl;
import com.app.ecomisiones.service.DetalleCompra.DetalleCompraServiceImpl;
import com.app.ecomisiones.service.MedioDePago.MedioDePagoServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import com.app.ecomisiones.service.Sucursal.SucursalServiceImpl;
import com.app.ecomisiones.service.Usuario.UsuarioServiceImpl;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private SucursalServiceImpl sucursalService;

    @Autowired
    private ProductoServiceImpl productoService;

    @Autowired
    private CompraServiceImpl compraService;

    @Autowired 
    private DetalleCompraServiceImpl detalleCompraService;

    @Autowired
    private MedioDePagoServiceImpl medioDePagoService;

    @Autowired
    private UsuarioServiceImpl usuarioService;

    // Método para ver mis compras
    @GetMapping("/misCompras")
    public String ver(Model model, Authentication auth) {
        try {

            Usuario usuario = (Usuario) auth.getPrincipal();
            List<Categoria> categorias = categoriaService.obtenerTodo();

            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("categorias", categorias);
            model.addAttribute("compras", usuario.getCompras());

            return "perfil/misCompras";

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return "redirect:/inicio";

        }
    }

    @GetMapping("/ver/{idProducto}/{cantidad}")
    public String getMethodName(@PathVariable int idProducto,
            @PathVariable int cantidad,
            Model modelo,
            Authentication auth) {

        try {

            Usuario usuario = (Usuario) auth.getPrincipal();
            List<Categoria> categorias = categoriaService.obtenerTodo();
            List<MedioDePago> medios = medioDePagoService.obtenerTodo();
            List<Sucursal> sucursales = sucursalService.obtenerTodo();

            Producto producto = productoService.buscarPorId(idProducto).orElse(null);

            if (producto == null) {
                throw new IllegalArgumentException("Error! El producto ingresado no existe.");
            }

            modelo.addAttribute("categorias", categorias);
            modelo.addAttribute("usuario", usuario);
            modelo.addAttribute("mediosPago", medios);
            modelo.addAttribute("sucursales", sucursales);
            modelo.addAttribute("total", producto.getPrecioConDescuento() * cantidad);
            modelo.addAttribute("producto", producto);
            modelo.addAttribute("cantidad", cantidad);

            return "caja";

        } catch (Exception e) {

            return ("redirect:/inicio");

        }
    }

    @PostMapping("/agregar")
    public String postMethodName(@RequestParam(name = "producto", required = false) Integer idProducto,
            @RequestParam(name = "cantidad", required = false) Integer cantidad,
            @RequestParam(name = "sucursal") int idSucursal,
            @RequestParam(name = "medioPago") MedioDePago medio,
            @RequestParam(name = "total") Double total,
            RedirectAttributes redirectAttributes,
            Authentication auth) {

        try {

            Sucursal sucursal = sucursalService.buscarPorId(idSucursal).orElse(null);
            if (medio == null || sucursal == null) {
                throw new IllegalArgumentException("Error! El medio de pago o la sucursal ingresada no existe.");
            }

            Usuario comprador = (Usuario) auth.getPrincipal();
            if (comprador == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            if (total == null) {
                throw new IllegalArgumentException("Error! El total ingresado es inválido.");
            }

            LocalDate fechaLLegada;
            String msj;
            if (LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY ||
                    LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY ||
                    LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY) {
                fechaLLegada = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                msj = "Se ha realizado la orden correctamente! El pedido llegará el próximo lunes " +
                        fechaLLegada;
            } else {
                fechaLLegada = LocalDate.now().plusDays(1);
                msj = "Se ha realizado la orden correctamente! El pedido llegará el dia de mañana " +
                        fechaLLegada;
            }

            Compra compra = new Compra(total, medio, sucursal, comprador, fechaLLegada);
            compraService.guardar(compra);

            comprador.agregarCompra(compra);

            Set<DetalleCompra> detalles = new HashSet<>();
            // Si es compra individual
            if (idProducto != null && cantidad != null) {

                Producto producto = productoService.buscarPorId(idProducto).orElse(null);
                if (producto == null) {
                    throw new IllegalArgumentException("Error! El producto ingresado no existe.");
                }

                DetalleCompra detalleCompra = new DetalleCompra(cantidad, compra, producto);
                detalleCompraService.guardar(detalleCompra);
                detalles.add(detalleCompra);

                producto.setStock(producto.getStock() - cantidad);
                productoService.modificar(producto);

            } else {
                // Si es compra por carrito
            }

            compra.agregarDetalles(detalles);
            //compraService.modificar(compra);

            redirectAttributes.addFlashAttribute("mensaje", msj);

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());

        }

        return "redirect:/inicio";
    }

}
