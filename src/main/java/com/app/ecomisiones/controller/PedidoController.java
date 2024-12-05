package com.app.ecomisiones.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Ciudad;
import com.app.ecomisiones.model.DetalleCarrito;
import com.app.ecomisiones.model.MedioDePago;
import com.app.ecomisiones.model.Pedido;
import com.app.ecomisiones.model.DetallePedido;
import com.app.ecomisiones.model.MedioDeEnvio;
import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.model.Sucursal;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.DetalleCarrito.DetalleCarritoServiceImpl;
import com.app.ecomisiones.service.DetallePedido.DetallePedidoServiceImpl;
import com.app.ecomisiones.service.MedioDePago.MedioDePagoServiceImpl;
import com.app.ecomisiones.service.Pedido.PedidoServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import com.app.ecomisiones.service.Sucursal.SucursalServiceImpl;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private CategoriaServiceImpl categoriaService;

    @Autowired
    private SucursalServiceImpl sucursalService;

    @Autowired
    private ProductoServiceImpl productoService;

    @Autowired
    private PedidoServiceImpl pedidoService;

    @Autowired
    private DetallePedidoServiceImpl detalleCompraService;

    @Autowired
    private MedioDePagoServiceImpl medioDePagoService;

    @Autowired
    private DetalleCarritoServiceImpl detalleCarritoService;

    // Método para ver mis pedidos
    @GetMapping("/misPedidos")
    public String ver(Model model, Authentication auth, RedirectAttributes redirectAttributes) {
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

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";

        }
    }

    @GetMapping("/preparar/{idProducto}/{cantidad}")
    public String getMethodName(@PathVariable int idProducto,
            @PathVariable int cantidad,
            Model modelo,
            Authentication auth, RedirectAttributes redirectAttributes) {

        try {

            Usuario usuario = (Usuario) auth.getPrincipal();
            List<Categoria> categorias = categoriaService.obtenerTodo();

            List<MedioDePago> medios = medioDePagoService.obtenerTodo();
            List<Sucursal> sucursales = sucursalService.obtenerTodo();

            Producto producto = productoService.buscarPorId(idProducto).orElse(null);
            if (producto == null) {
                throw new IllegalArgumentException("Error! El producto ingresado no existe.");
            }

            Map<Producto, Integer> detalles = new HashMap<>();
            detalles.put(producto, cantidad);
            modelo.addAttribute("detalles", detalles);

            modelo.addAttribute("categorias", categorias);
            modelo.addAttribute("usuario", usuario);

            modelo.addAttribute("mediosPago", medios);
            modelo.addAttribute("sucursales", sucursales);

            modelo.addAttribute("producto", producto);
            modelo.addAttribute("cantidad", cantidad);
            modelo.addAttribute("total", producto.getPrecioConDescuento() * cantidad);

            double costo = 0;
            int diasDeEspera = 0;
            if (usuario.getSucursalMasCercana() != null) {
                Sucursal sucursal = usuario.getSucursalMasCercana();
                // Obtener medio de envio
                MedioDeEnvio medioDeEnvio = obtenerEnvio(producto, sucursal);

                costo = medioDeEnvio.getCosto();
                diasDeEspera = medioDeEnvio.getDiasDeEspera();
            }
            modelo.addAttribute("costoEnvio", costo);
            modelo.addAttribute("diasEspera", diasDeEspera);

            return "caja";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return ("redirect:/inicio");

        }
    }

    @GetMapping("/preparar")
    public String prepararDesdeCarrito(Model modelo,
            Authentication auth, RedirectAttributes redirectAttributes) {

        try {

            Usuario usuario = (Usuario) auth.getPrincipal();
            List<Categoria> categorias = categoriaService.obtenerTodo();

            List<MedioDePago> medios = medioDePagoService.obtenerTodo();
            List<Sucursal> sucursales = sucursalService.obtenerTodo();

            double total = 0;

            Map<Producto, Integer> detalles = new HashMap<>();
            for (DetalleCarrito detalleCarrito : usuario.getCarrito().getDetalles()) {
                detalles.put(detalleCarrito.getProducto(), detalleCarrito.getCantidad());
                total += detalleCarrito.getProducto().getPrecioConDescuento() * detalleCarrito.getCantidad();
            }
            modelo.addAttribute("detalles", detalles);

            modelo.addAttribute("categorias", categorias);
            modelo.addAttribute("usuario", usuario);

            modelo.addAttribute("mediosPago", medios);
            modelo.addAttribute("sucursales", sucursales);

            modelo.addAttribute("total", total);

            double costo = 0;
            int diasDeEspera = 0;
            if (usuario.getSucursalMasCercana() != null) {
                Sucursal sucursal = usuario.getSucursalMasCercana();
                List<Ciudad> ciudadesRepetidas = new ArrayList<>();

                for (DetalleCarrito detalleCarrito : usuario.getCarrito().getDetalles()) {
                    if (!ciudadesRepetidas.contains(detalleCarrito.getProducto().getAlmacen().getCiudad())) {
                        // Obtener medio de envio
                        MedioDeEnvio medioDeEnvio = obtenerEnvio(detalleCarrito.getProducto(), sucursal);
                        costo += medioDeEnvio.getCosto();
                        diasDeEspera += medioDeEnvio.getDiasDeEspera();

                        ciudadesRepetidas.add(detalleCarrito.getProducto().getAlmacen().getCiudad());
                    }
                }
            }
            modelo.addAttribute("costoEnvio", costo);
            modelo.addAttribute("diasEspera", diasDeEspera);

            return "caja";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return ("redirect:/inicio");

        }
    }

    @PostMapping("/agregar")
    public String postMethodName(@RequestParam(name = "producto", required = false) Integer idProducto,
            @RequestParam(name = "cantidad", required = false) Integer cantidad,
            @RequestParam(name = "sucursal") int idSucursal,
            @RequestParam(name = "medioPago") int idMedioDePago,
            @RequestParam(name = "total") Double total,
            @RequestParam(name = "costoEnvio") Double costoEnvio,
            @RequestParam(name = "diasEspera") Integer diasEspera,
            RedirectAttributes redirectAttributes,
            Authentication auth) {
        try {

            Sucursal sucursal = sucursalService.buscarPorId(idSucursal).orElse(null);
            if (sucursal == null) {
                throw new IllegalArgumentException("Error! La sucursal ingresada no existe.");
            }

            MedioDePago medioDePago = medioDePagoService.buscarPorId(idMedioDePago).orElse(null);
            if (medioDePago == null) {
                throw new IllegalArgumentException("Error! El medio de pago ingresado no existe.");
            }

            Usuario comprador = (Usuario) auth.getPrincipal();
            if (comprador == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            if (total == null) {
                throw new IllegalArgumentException("Error! El total ingresado es inválido.");
            }

            System.out.println(costoEnvio);

            if (costoEnvio == null) {
                throw new IllegalArgumentException("Error! El costo de envío ingresado es inválido.");
            }

            if (diasEspera == null) {
                throw new IllegalArgumentException("Error! La cantidad de días de espera ingresada es inválida.");
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

            Pedido pedido = new Pedido(total, costoEnvio, diasEspera, medioDePago, sucursal, comprador, fechaLLegada);
            pedidoService.guardar(pedido);

            comprador.agregarCompra(pedido);

            Set<DetallePedido> detalles = new HashSet<>();
            MedioDeEnvio medioDeEnvio = null;
            // Si es compra individual
            if (idProducto != null && cantidad != null) {

                Producto producto = productoService.buscarPorId(idProducto).orElse(null);
                if (producto == null) {
                    throw new IllegalArgumentException("Error! El producto ingresado no existe.");
                } else if (sucursal != null) {
                    medioDeEnvio = obtenerEnvio(producto, sucursal);
                }

                DetallePedido detallePedido = new DetallePedido(cantidad, producto.getPeso() * cantidad, medioDeEnvio,
                        pedido, producto);
                detalleCompraService.guardar(detallePedido);
                detalles.add(detallePedido);

                producto.setStock(producto.getStock() - cantidad);
                productoService.modificar(producto);

            } else {
                // Si es compra por carrito

                for (DetalleCarrito detalleCarrito : comprador.getCarrito().getDetalles()) {

                    medioDeEnvio = obtenerEnvio(detalleCarrito.getProducto(), sucursal);

                    DetallePedido detallePedido = new DetallePedido(detalleCarrito.getCantidad(),
                            detalleCarrito.getProducto().getPeso() * detalleCarrito.getCantidad(), medioDeEnvio,
                            pedido, detalleCarrito.getProducto());
                    detalleCompraService.guardar(detallePedido);
                    detalles.add(detallePedido);

                    detalleCarrito.marcarInactivo();
                    detalleCarritoService.borrar(detalleCarrito);
                }

            }

            pedido.agregarDetalles(detalles);
            // compraService.modificar(compra);

            redirectAttributes.addFlashAttribute("mensaje", msj);

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());

        }

        return "redirect:/inicio";
    }

    @GetMapping("/calcularEnvio")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calcularEnvio(@RequestParam Integer sucursalId,
            @RequestParam(required = false) Integer productoId, Authentication auth) {

        try {
            // Obtener el producto y la sucursal
            if (sucursalId == null) {
                throw new IllegalArgumentException("Error! El ID de sucursal ingresado es inválido.");
            }

            Sucursal sucursal = sucursalService.buscarPorId(sucursalId).orElse(null);
            if (sucursal == null) {
                throw new IllegalArgumentException("Error! La sucursal ingresada no existe.");
            }

            // Calcular el costo y días de espera
            double costo = 0;
            int diasDeEspera = 0;

            if (productoId == null) {

                Usuario usuario = (Usuario) auth.getPrincipal();

                Set<Ciudad> ciudadesRepetidas = new HashSet<>();
                for (DetalleCarrito detalleCarrito : usuario.getCarrito().getDetalles()) {
                    if (!ciudadesRepetidas.contains(detalleCarrito.getProducto().getAlmacen().getCiudad())) {
                        // Obtener medio de envio
                        MedioDeEnvio medioDeEnvio = obtenerEnvio(detalleCarrito.getProducto(), sucursal);

                        costo += medioDeEnvio.getCosto();
                        diasDeEspera += medioDeEnvio.getDiasDeEspera();

                        ciudadesRepetidas.add(detalleCarrito.getProducto().getAlmacen().getCiudad());
                    }
                }

            } else {

                Producto producto = productoService.buscarPorId(productoId).orElse(null);
                if (producto == null) {
                    throw new IllegalArgumentException("Error! El producto ingresado no existe.");
                }

                // Obtener medio de envio
                MedioDeEnvio medioDeEnvio = obtenerEnvio(producto, sucursal);

                costo = medioDeEnvio.getCosto();
                diasDeEspera = medioDeEnvio.getDiasDeEspera();

            }

            // Crear un mapa con los datos
            Map<String, Object> response = new HashMap<>();
            response.put("costoEnvio", costo);
            response.put("diasEspera", diasDeEspera);

            // Devolver la respuesta como JSON
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private MedioDeEnvio obtenerEnvio(Producto producto, Sucursal sucursal) {

        Ciudad ciudadProducto = producto.getAlmacen().getCiudad();
        Ciudad ciudadSucursal = sucursal.getCiudad();

        if (ciudadProducto.getId() == ciudadSucursal.getId()) {
            return MedioDeEnvio.Local;
        } else if (ciudadProducto.getProvincia().getId() == ciudadSucursal.getProvincia().getId()) {
            return MedioDeEnvio.Provincial;
        } else {
            return MedioDeEnvio.Nacional;
        }
    }

}
