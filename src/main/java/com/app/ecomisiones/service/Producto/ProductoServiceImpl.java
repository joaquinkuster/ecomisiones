package com.app.ecomisiones.service.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.repository.ProductoRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones sobre la entidad {@link Producto}.
 * Esta clase proporciona las operaciones CRUD básicas (Crear, Leer, Actualizar, Eliminar)
 * para la entidad Producto, además de funcionalidades adicionales como búsqueda por categoría,
 * productos recientes, productos más vendidos, y productos con bajo stock.
 */
@Service
public class ProductoServiceImpl implements ProductoService, CrudService<Producto> {

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Guarda un nuevo producto en la base de datos.
     * 
     * @param producto El producto a guardar.
     * @return El producto guardado.
     */
    @Override
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Busca un producto por su identificador.
     * Solo devuelve productos activos (no marcados como inactivos).
     * 
     * @param id El identificador del producto.
     * @return Un {@link Optional} con el producto encontrado, si está activo.
     */
    @Override
    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .filter(producto -> !producto.esInactivo()); // Solo devuelve productos activos
    }

    /**
     * Obtiene todos los productos activos.
     * 
     * @return Una lista de todos los productos activos.
     */
    @Override
    public List<Producto> obtenerTodo() {
        return productoRepository.findByBajaFalse(); // Solo devuelve productos activos
    }

    /**
     * Modifica un producto existente.
     * 
     * @param producto El producto con los datos actualizados.
     * @return El producto modificado.
     */
    @Override
    public Producto modificar(Producto producto) {
        return productoRepository.save(producto); // Guarda el producto modificado
    }

    /**
     * Marca un producto como inactivo, en lugar de eliminarlo de la base de datos.
     * 
     * @param producto El producto a marcar como inactivo.
     */
    @Override
    public void borrar(Producto producto) {
        producto.marcarInactivo(); // Marca el producto como inactivo
        productoRepository.save(producto); // Guarda el producto marcado como inactivo
    }

    /**
     * Verifica si un producto con el identificador dado existe y está activo.
     * 
     * @param id El identificador del producto.
     * @return true si el producto existe y está activo, false en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return productoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta productos activos
    }

    /**
     * Busca los productos de una categoría específica.
     * Solo devuelve productos activos de la categoría proporcionada.
     * 
     * @param categoria La categoría de productos a buscar.
     * @return Una lista de productos activos de la categoría dada.
     */
    @Override
    public List<Producto> buscarPorCategoria(Categoria categoria) {
        return productoRepository.findByCategoriaAndBajaFalse(categoria); // Solo devuelve productos activos
    }

    /**
     * Obtiene los productos más recientes (ordenados por fecha de creación).
     * 
     * @return Una lista de productos más recientes, excluyendo los inactivos.
     */
    @Override
    public List<Producto> buscarRecientes() {
        return productoRepository.findByBajaFalseOrderByFechaCreacionDesc();
    }

    /**
     * Obtiene los productos con menor stock.
     * 
     * @return Una lista de productos con el stock más bajo, excluyendo los inactivos.
     */
    @Override
    public List<Producto> buscarUltimosDisponibles() {
        return productoRepository.findByBajaFalseOrderByStockAsc();
    }

    /**
     * Obtiene los productos más vendidos.
     * 
     * @return Una lista de los productos más vendidos, excluyendo los inactivos.
     */
    @Override
    public List<Producto> buscarMasVendidos() {
        return productoRepository.findByBajaFalseOrderByVentasDesc();
    }
}
