package com.app.ecomisiones.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador encargado de gestionar las solicitudes relacionadas con los
 * pedidos de productos.
 * Permite agregar pedidos, calcular el costo y los días de espera de un envío,
 * y manejar los detalles de cada compra realizada por los usuarios.
 * 
 * Las operaciones incluyen:
 * - Agregar un nuevo pedido con los productos seleccionados.
 * - Calcular los costos y tiempos de entrega según la sucursal y el producto.
 */
@Controller
@RequestMapping("/pedido")
public class PedidoController {

    // Servicios inyectados para interactuar con la lógica de negocio
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

    /**
     * Método para ver los pedidos realizados por el usuario.
     * 
     * @param model              El modelo donde se almacenan los atributos a pasar
     *                           a la vista.
     * @param auth               La autenticación del usuario actual.
     * @param redirectAttributes Atributos de redirección para mostrar mensajes de
     *                           error.
     * @return El nombre de la vista correspondiente o redirección en caso de error.
     */
    @GetMapping("/misPedidos")
    public String verMisPedidos(Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            // Obtener el usuario actual desde la autenticación
            Usuario usuario = (Usuario) auth.getPrincipal();

            // Obtener todas las categorías para mostrar en la vista
            List<Categoria> categorias = categoriaService.obtenerTodo();

            // Validar si el usuario es nulo
            if (usuario == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            // Agregar los atributos al modelo
            model.addAttribute("usuario", usuario);
            model.addAttribute("categorias", categorias);
            model.addAttribute("compras", usuario.getCompras());
            model.addAttribute("titulo", "Mis pedidos");

            return "pages/perfil/misPedidos"; // Retorna la vista de mis pedidos

        } catch (Exception e) {
            // En caso de error, agregar un mensaje y redirigir al inicio
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    /**
     * Método para preparar un pedido desde el carrito del usuario.
     * 
     * @param idProducto         El ID del producto a agregar al pedido (opcional).
     * @param cantidad           La cantidad del producto a agregar (opcional).
     * @param modelo             El modelo para agregar atributos a la vista.
     * @param auth               La autenticación del usuario actual.
     * @param redirectAttributes Atributos de redirección para mostrar mensajes de
     *                           error.
     * @return El nombre de la vista correspondiente o redirección en caso de error.
     */
    @GetMapping("/preparar")
    public String verFormularioPedido(@RequestParam(name = "producto", required = false) Integer idProducto,
            @RequestParam(name = "cantidad", required = false) Integer cantidad,
            Model modelo,
            Authentication auth,
            RedirectAttributes redirectAttributes) {
        try {
            // Obtener el usuario actual desde la autenticación
            Usuario usuario = (Usuario) auth.getPrincipal();

            // Obtener todas las categorías, medios de pago y sucursales para mostrar en la
            // vista
            List<Categoria> categorias = categoriaService.obtenerTodo();
            List<MedioDePago> medios = medioDePagoService.obtenerTodo();
            List<Sucursal> sucursales = sucursalService.obtenerTodo();

            // Variables para calcular el total del pedido
            double total = 0;
            Map<Producto, Integer> detalles = new HashMap<>();

            // Verificar si el producto y cantidad han sido proporcionados
            if (idProducto != null && cantidad != null) {
                // Buscar el producto en la base de datos
                Producto producto = productoService.buscarPorId(idProducto).orElse(null);

                // Validar si el producto existe
                if (producto == null) {
                    throw new IllegalArgumentException("Error! El producto ingresado no existe.");
                }

                // Agregar el producto al detalle y calcular el total
                detalles.put(producto, cantidad);
                total = producto.getPrecioConDescuento() * cantidad;

                modelo.addAttribute("producto", producto);
                modelo.addAttribute("cantidad", cantidad);
            } else {
                // Si no se especifica un producto, se agregan los productos del carrito
                for (DetalleCarrito detalleCarrito : usuario.getCarrito().getDetalles()) {
                    detalles.put(detalleCarrito.getProducto(), detalleCarrito.getCantidad());
                    total += detalleCarrito.getProducto().getPrecioConDescuento() * detalleCarrito.getCantidad();
                }
            }

            // Agregar atributos al modelo
            modelo.addAttribute("detalles", detalles);
            modelo.addAttribute("categorias", categorias);
            modelo.addAttribute("usuario", usuario);
            modelo.addAttribute("mediosPago", medios);
            modelo.addAttribute("sucursales", sucursales);
            modelo.addAttribute("total", Math.round(total * 100.0) / 100.0);

            // Calcular costos de envío y días de espera
            double costo = 0;
            int diasDeEspera = 0;
            if (usuario.getSucursalMasCercana() != null) {
                Sucursal sucursal = usuario.getSucursalMasCercana();

                // Si se especificó un producto y cantidad, se calcula el envío solo para ese
                // producto
                if (idProducto != null && cantidad != null) {
                    Producto producto = productoService.buscarPorId(idProducto).orElse(null);
                    if (producto == null) {
                        throw new IllegalArgumentException("Error! El producto ingresado no existe.");
                    }
                    // Obtener el medio de envío para el producto
                    MedioDeEnvio medioDeEnvio = obtenerEnvio(producto, sucursal);
                    costo = medioDeEnvio.getCosto();
                    diasDeEspera = medioDeEnvio.getDiasDeEspera();
                } else {
                    // Si no se especificó un producto, calcular el envío basado en las ciudades de
                    // los productos en el carrito
                    Map<Double, Integer> datosEnvio = calcularCostoYDiasDeEsperaPorCarrito(
                            usuario, sucursal);
                    for (Map.Entry<Double, Integer> entrada : datosEnvio.entrySet()) {
                        costo += entrada.getKey();
                        diasDeEspera += entrada.getValue();
                    }
                }
            }

            // Agregar el costo de envío y los días de espera al modelo
            modelo.addAttribute("costoEnvio", Math.round(costo * 100.0) / 100.0);
            modelo.addAttribute("diasEspera", diasDeEspera);
            modelo.addAttribute("titulo", "Preparar pedido");

            return "pages/caja"; // Retorna la vista de preparación de pedido

        } catch (Exception e) {
            // En caso de error, agregar un mensaje y redirigir al inicio
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
            return "redirect:/inicio";
        }
    }

    /**
     * Este método maneja la solicitud para agregar un nuevo pedido.
     * Se encarga de validar los parámetros y registrar el pedido con sus detalles.
     *
     * @param idProducto         ID del producto que se está comprando (puede ser
     *                           nulo si se está utilizando el carrito).
     * @param cantidad           Cantidad del producto que se desea comprar.
     * @param idSucursal         ID de la sucursal donde se procesará el pedido.
     * @param idMedioDePago      ID del medio de pago seleccionado.
     * @param total              Total de la compra (producto + envío).
     * @param costoEnvio         Costo de envío asociado con el pedido.
     * @param diasEspera         Número de días de espera estimados para el envío.
     * @param redirectAttributes Atributos para redirigir el mensaje de éxito o
     *                           error.
     * @param auth               Información de autenticación del usuario.
     * @return La vista de redirección al inicio con el mensaje adecuado.
     */
    @PostMapping("/agregar")
    public String agregarPedido(@RequestParam(name = "producto", required = false) Integer idProducto,
            @RequestParam(name = "cantidad", required = false) Integer cantidad,
            @RequestParam(name = "sucursal") int idSucursal,
            @RequestParam(name = "medioPago") int idMedioDePago,
            @RequestParam(name = "total") Double total,
            @RequestParam(name = "costoEnvio") Double costoEnvio,
            @RequestParam(name = "diasEspera") Integer diasEspera,
            RedirectAttributes redirectAttributes,
            Authentication auth) {

        try {
            // Validación de la sucursal
            Sucursal sucursal = sucursalService.buscarPorId(idSucursal).orElse(null);
            if (sucursal == null) {
                throw new IllegalArgumentException("Error! La sucursal ingresada no existe.");
            }

            // Validación del medio de pago
            MedioDePago medioDePago = medioDePagoService.buscarPorId(idMedioDePago).orElse(null);
            if (medioDePago == null) {
                throw new IllegalArgumentException("Error! El medio de pago ingresado no existe.");
            }

            // Obtener usuario autenticado
            Usuario comprador = (Usuario) auth.getPrincipal();
            if (comprador == null) {
                throw new IllegalArgumentException("Error! El usuario ingresado no existe.");
            }

            // Validación del total
            if (total == null) {
                throw new IllegalArgumentException("Error! El total ingresado es inválido.");
            }

            // Validación del costo de envío
            if (costoEnvio == null) {
                throw new IllegalArgumentException("Error! El costo de envío ingresado es inválido.");
            }

            // Validación de los días de espera
            if (diasEspera == null) {
                throw new IllegalArgumentException("Error! La cantidad de días de espera ingresada es inválida.");
            }

            // Calcular la fecha de llegada del pedido
            LocalDate fechaLlegada;
            String mensaje;
            if (LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY ||
                    LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY ||
                    LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY) {
                fechaLlegada = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                mensaje = "Se ha realizado la orden correctamente! El pedido llegará el próximo lunes " + fechaLlegada;
            } else {
                fechaLlegada = LocalDate.now().plusDays(1);
                mensaje = "Se ha realizado la orden correctamente! El pedido llegará el día de mañana " + fechaLlegada;
            }

            // Crear el pedido
            Pedido pedido = new Pedido(total, costoEnvio, diasEspera, medioDePago, sucursal, comprador, fechaLlegada);
            pedidoService.guardar(pedido);
            comprador.agregarCompra(pedido);

            Set<DetallePedido> detalles = new HashSet<>();
            MedioDeEnvio medioDeEnvio = null;

            // Si es compra individual
            if (idProducto != null && cantidad != null) {
                Producto producto = productoService.buscarPorId(idProducto).orElse(null);
                if (producto == null) {
                    throw new IllegalArgumentException("Error! El producto ingresado no existe.");
                } else {
                    medioDeEnvio = obtenerEnvio(producto, sucursal);
                }

                // Crear el detalle del pedido
                DetallePedido detallePedido = new DetallePedido(cantidad, producto.getPeso() * cantidad, medioDeEnvio,
                        pedido, producto);
                detalleCompraService.guardar(detallePedido);
                detalles.add(detallePedido);

                // Actualizar el stock del producto
                producto.setStock(producto.getStock() - cantidad);
                productoService.modificar(producto);

            } else {
                // Si es compra por carrito
                for (DetalleCarrito detalleCarrito : comprador.getCarrito().getDetalles()) {
                    medioDeEnvio = obtenerEnvio(detalleCarrito.getProducto(), sucursal);

                    // Crear el detalle del pedido
                    DetallePedido detallePedido = new DetallePedido(detalleCarrito.getCantidad(),
                            detalleCarrito.getProducto().getPeso() * detalleCarrito.getCantidad(), medioDeEnvio,
                            pedido, detalleCarrito.getProducto());
                    detalleCompraService.guardar(detallePedido);
                    detalles.add(detallePedido);

                    // Marcar como inactivo el detalle de carrito y eliminarlo
                    detalleCarrito.marcarInactivo();
                    detalleCarritoService.borrar(detalleCarrito);
                }
            }

            // Agregar los detalles al pedido
            pedido.agregarDetalles(detalles);
            redirectAttributes.addFlashAttribute("mensaje", mensaje);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje", e.getMessage());
        }

        return "redirect:/inicio";
    }

    /**
     * Este método calcula el costo y los días de espera del envío en base a la
     * sucursal y el producto.
     * Devuelve la información en formato JSON.
     *
     * @param sucursalId ID de la sucursal.
     * @param productoId ID del producto a calcular el envío (opcional).
     * @param auth       Información de autenticación del usuario.
     * @return Un mapa con el costo del envío y los días de espera.
     */
    @GetMapping("/calcularEnvio")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calcularEnvio(@RequestParam Integer sucursalId,
            @RequestParam(required = false) Integer productoId,
            Authentication auth) {
        try {
            // Validar la sucursal
            if (sucursalId == null) {
                throw new IllegalArgumentException("Error! El ID de sucursal ingresado es inválido.");
            }

            Sucursal sucursal = sucursalService.buscarPorId(sucursalId).orElse(null);
            if (sucursal == null) {
                throw new IllegalArgumentException("Error! La sucursal ingresada no existe.");
            }

            // Calcular el costo y los días de espera
            double costo = 0;
            int diasDeEspera = 0;

            if (productoId == null) {
                Map<Double, Integer> datosEnvio = calcularCostoYDiasDeEsperaPorCarrito((Usuario) auth.getPrincipal(),
                        sucursal);
                for (Map.Entry<Double, Integer> entrada : datosEnvio.entrySet()) {
                    costo += entrada.getKey();
                    diasDeEspera += entrada.getValue();
                }
            } else {
                // Si se pasa un producto, se calcula su costo de envío
                Producto producto = productoService.buscarPorId(productoId).orElse(null);
                if (producto == null) {
                    throw new IllegalArgumentException("Error! El producto ingresado no existe.");
                }
                MedioDeEnvio medioDeEnvio = obtenerEnvio(producto, sucursal);
                costo = medioDeEnvio.getCosto();
                diasDeEspera = medioDeEnvio.getDiasDeEspera();
            }

            // Crear respuesta con los datos de costo y días de espera
            Map<String, Object> response = new HashMap<>();
            response.put("costoEnvio", costo);
            response.put("diasEspera", diasDeEspera);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    /**
     * Este método determina el medio de envío a utilizar según la ciudad del
     * producto y la sucursal.
     * 
     * @param producto El producto a enviar.
     * @param sucursal La sucursal destino.
     * @return El medio de envío correspondiente.
     */
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

    /**
     * Calcula el costo total de envío y los días de espera para los productos en el
     * carrito de un usuario.
     * Se toma en cuenta el medio de envío disponible en función de la ciudad del
     * almacén y la sucursal.
     *
     * @param usuario  El usuario cuyo carrito de compras se va a procesar.
     * @param sucursal La sucursal que se utiliza para calcular el medio de envío.
     * @return Un mapa con el costo total de envío y los días de espera, donde la
     *         clave es el costo y el valor son los días de espera.
     */
    private Map<Double, Integer> calcularCostoYDiasDeEsperaPorCarrito(Usuario usuario, Sucursal sucursal) {
        // Inicializa las variables para el costo y los días de espera acumulados
        double costo = 0;
        int diasDeEspera = 0;

        // Lista para llevar un seguimiento de las ciudades ya procesadas
        List<Ciudad> ciudadesRepetidas = new ArrayList<>();

        // Itera sobre los detalles del carrito del usuario
        for (DetalleCarrito detalleCarrito : usuario.getCarrito().getDetalles()) {
            // Obtiene la ciudad del almacén del producto
            Ciudad ciudad = detalleCarrito.getProducto().getAlmacen().getCiudad();

            // Asegura que la ciudad no sea nula
            if (ciudad != null) {
                // Verifica si esta ciudad ya ha sido procesada
                boolean ciudadProcesada = false;
                for (Ciudad c : ciudadesRepetidas) {
                    // Si la ciudad ya está en la lista, marca que ya fue procesada
                    if (c.getId() == ciudad.getId()) {
                        ciudadProcesada = true;
                        break;
                    }
                }

                // Si la ciudad no ha sido procesada, la procesamos
                if (!ciudadProcesada) {
                    // Obtiene el medio de envío disponible para el producto y la sucursal
                    MedioDeEnvio medioDeEnvio = obtenerEnvio(detalleCarrito.getProducto(), sucursal);

                    // Si el medio de envío es válido, acumulamos el costo y los días de espera
                    if (medioDeEnvio != null) {
                        costo += medioDeEnvio.getCosto(); // Suma el costo del envío
                        diasDeEspera += medioDeEnvio.getDiasDeEspera(); // Suma los días de espera del envío
                    }

                    // Marca la ciudad como procesada para evitar procesarla nuevamente
                    ciudadesRepetidas.add(ciudad);
                }
            }
        }

        // Crea un mapa con el costo redondeado a 2 decimales y los días de espera
        // acumulados
        Map<Double, Integer> response = new HashMap<>();
        response.put(Math.round(costo * 100.0) / 100.0, diasDeEspera);

        // Devuelve el mapa con los resultados
        return response;
    }
}