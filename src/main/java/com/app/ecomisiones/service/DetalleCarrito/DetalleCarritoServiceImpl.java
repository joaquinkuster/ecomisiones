package com.app.ecomisiones.service.DetalleCarrito;

import com.app.ecomisiones.model.DetalleCarrito;
import com.app.ecomisiones.repository.DetalleCarritoRepository;
import com.app.ecomisiones.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones sobre la entidad {@link DetalleCarrito}.
 * Esta clase implementa las operaciones básicas de CRUD (Crear, Leer, Actualizar, Eliminar) 
 * para la entidad DetalleCarrito.
 */
@Service
public class DetalleCarritoServiceImpl implements DetalleCarritoService, CrudService<DetalleCarrito> {

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    /**
     * Guarda un nuevo detalle de carrito en la base de datos.
     * 
     * @param detalleCarrito El detalle de carrito a guardar.
     * @return El detalle de carrito guardado.
     */
    @Override
    public DetalleCarrito guardar(DetalleCarrito detalleCarrito) {
        return detalleCarritoRepository.save(detalleCarrito);
    }

    /**
     * Busca un detalle de carrito por su identificador.
     * Solo devuelve detalles de carrito activos (no marcados como inactivos).
     * 
     * @param id El identificador del detalle de carrito.
     * @return Un Optional con el detalle de carrito encontrado, si está activo.
     */
    @Override
    public Optional<DetalleCarrito> buscarPorId(Integer id) {
        return detalleCarritoRepository.findById(id)
                .filter(detalleCarrito -> !detalleCarrito.esInactivo()); // Solo devuelve detalles activos
    }

    /**
     * Obtiene todos los detalles de carrito activos.
     * 
     * @return Una lista de detalles de carrito activos.
     */
    @Override
    public List<DetalleCarrito> obtenerTodo() {
        return detalleCarritoRepository.findByBajaFalse(); // Solo devuelve detalles activos
    }

    /**
     * Modifica un detalle de carrito existente.
     * 
     * @param detalleCarrito El detalle de carrito con los datos actualizados.
     * @return El detalle de carrito modificado.
     */
    @Override
    public DetalleCarrito modificar(DetalleCarrito detalleCarrito) {
        // Otros campos actualizables
        return detalleCarritoRepository.save(detalleCarrito);
    }

    /**
     * Elimina un detalle de carrito. 
     * Esto no lo elimina físicamente de la base de datos, sino que lo marca como inactivo.
     * 
     * @param detalleCarrito El detalle de carrito a eliminar.
     */
    @Override
    public void borrar(DetalleCarrito detalleCarrito) {
        detalleCarrito.marcarInactivo();
        detalleCarritoRepository.save(detalleCarrito);
    }

    /**
     * Verifica si un detalle de carrito con el identificador dado existe y está activo.
     * 
     * @param id El identificador del detalle de carrito.
     * @return true si el detalle de carrito existe y está activo, false en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return detalleCarritoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta detalles activos
    }
}
