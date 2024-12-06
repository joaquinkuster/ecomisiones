package com.app.ecomisiones.controller;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.DetalleCarrito;
import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Carrito.CarritoServiceImpl;
import com.app.ecomisiones.service.Categoria.CategoriaServiceImpl;
import com.app.ecomisiones.service.DetalleCarrito.DetalleCarritoServiceImpl;
import com.app.ecomisiones.service.Producto.ProductoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;





public class CarritoControllerTest {

    @Mock
    private DetalleCarritoServiceImpl detalleCarritoService;

    @Mock
    private ProductoServiceImpl productoService;

    @Mock
    private CategoriaServiceImpl categoriaService;

    @Mock
    private CarritoServiceImpl carritoService;

    @Mock
    private Model model;

    @Mock
    private Authentication auth;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private CarritoController carritoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVerCarrito() {
        Usuario usuario = mock(Usuario.class);
        List<Categoria> categorias = new ArrayList<>();
        when(auth.getPrincipal()).thenReturn(usuario);
        when(categoriaService.obtenerTodo()).thenReturn(categorias);

        String viewName = carritoController.ver(model, auth, redirectAttributes);

        verify(model).addAttribute("usuario", usuario);
        verify(model).addAttribute("categorias", categorias);
        verify(model).addAttribute("carrito", usuario.getCarrito());
        verify(model).addAttribute("total", usuario.getCarrito().calcularTotal());
        assertEquals("carrito", viewName);
    }

    @Test
    public void testAgregarProductoAlCarrito() {
        Usuario usuario = mock(Usuario.class);
        Producto producto = mock(Producto.class);
        when(auth.getPrincipal()).thenReturn(usuario);
        when(productoService.buscarPorId(anyInt())).thenReturn(Optional.of(producto));
        when(producto.getStock()).thenReturn(10);

        String viewName = carritoController.agregar(1, 2, auth, redirectAttributes);

        verify(detalleCarritoService).guardar(any(DetalleCarrito.class));
        verify(productoService).modificar(producto);
        verify(carritoService).modificar(usuario.getCarrito());
        verify(redirectAttributes).addFlashAttribute("mensaje", "Hecho! El producto se ha agregado al carrito correctamente.");
        assertEquals("redirect:/producto/ver/1", viewName);
    }

    @Test
    public void testEliminarProductoDelCarrito() {
        Usuario usuario = mock(Usuario.class);
        Producto producto = mock(Producto.class);
        DetalleCarrito detalle = mock(DetalleCarrito.class);
        Set<DetalleCarrito> detalles = Set.of(detalle);
        when(auth.getPrincipal()).thenReturn(usuario);
        when(productoService.buscarPorId(anyInt())).thenReturn(Optional.of(producto));
        when(usuario.getCarrito().getDetalles()).thenReturn(detalles);
        when(detalle.getProducto()).thenReturn(producto);
        when(producto.getId()).thenReturn(1);

        String viewName = carritoController.eliminar(1, null, auth, redirectAttributes);

        verify(detalle).marcarInactivo();
        verify(productoService).modificar(producto);
        verify(detalleCarritoService).borrar(detalle);
        verify(carritoService).modificar(usuario.getCarrito());
        verify(redirectAttributes).addFlashAttribute("mensaje", "Hecho! El producto se ha eliminado del carrito correctamente.");
        assertEquals("redirect:/producto/ver/1", viewName);
    }
}