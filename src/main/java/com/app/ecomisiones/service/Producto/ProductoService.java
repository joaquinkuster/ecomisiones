package com.app.ecomisiones.service.Producto;

import java.util.List;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Producto;

/**
 * Servicio para gestionar las operaciones relacionadas con los productos.
 * Proporciona métodos para realizar consultas de productos según diferentes criterios
 * como categoría, fecha de creación, disponibilidad o ventas.
 */
public interface ProductoService {

    /**
     * Busca los productos asociados a una categoría específica.
     * 
     * @param categoria la categoría cuyos productos se desean consultar
     * @return una lista de productos que pertenecen a la categoría indicada
     */
    List<Producto> buscarPorCategoria(Categoria categoria);

    /**
     * Busca los productos más recientes.
     * 
     * @return una lista de los productos más recientes, ordenados por fecha de creación
     */
    List<Producto> buscarRecientes();

    /**
     * Busca los productos con menos stock disponible.
     * 
     * @return una lista de los productos con menor cantidad disponible, ordenados por stock ascendente
     */
    List<Producto> buscarUltimosDisponibles();

    /**
     * Busca los productos más vendidos.
     * 
     * @return una lista de los productos más vendidos, ordenados por ventas de mayor a menor
     */
    List<Producto> buscarMasVendidos();
}
