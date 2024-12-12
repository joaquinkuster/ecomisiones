package com.app.ecomisiones.service.DetallePedido;

import com.app.ecomisiones.model.DetallePedido;
import com.app.ecomisiones.repository.DetallePedidoRepository;
import com.app.ecomisiones.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones sobre la entidad {@link DetallePedido}.
 * Esta clase implementa las operaciones básicas de CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad DetallePedido.
 */
@Service
public class DetallePedidoServiceImpl implements DetallePedidoService, CrudService<DetallePedido> {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    /**
     * Guarda un nuevo detalle de pedido en la base de datos.
     * 
     * @param detallePedido El detalle de pedido a guardar.
     * @return El detalle de pedido guardado.
     */
    @Override
    public DetallePedido guardar(DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    /**
     * Busca un detalle de pedido por su identificador.
     * Solo devuelve detalles de pedido activos (no marcados como inactivos).
     * 
     * @param id El identificador del detalle de pedido.
     * @return Un Optional con el detalle de pedido encontrado, si está activo.
     */
    @Override
    public Optional<DetallePedido> buscarPorId(Integer id) {
        return detallePedidoRepository.findById(id)
                .filter(detallePedido -> !detallePedido.esInactivo()); // Solo devuelve detalles activos
    }

    /**
     * Obtiene todos los detalles de pedido activos.
     * 
     * @return Una lista de detalles de pedido activos.
     */
    @Override
    public List<DetallePedido> obtenerTodo() {
        return detallePedidoRepository.findByBajaFalse(); // Solo devuelve detalles activos
    }

    /**
     * Modifica un detalle de pedido existente.
     * 
     * @param detallePedido El detalle de pedido con los datos actualizados.
     * @return El detalle de pedido modificado.
     */
    @Override
    public DetallePedido modificar(DetallePedido detallePedido) {
        // Otros campos actualizables
        return detallePedidoRepository.save(detallePedido);
    }

    /**
     * Elimina un detalle de pedido. 
     * Esto no lo elimina físicamente de la base de datos, sino que lo marca como inactivo.
     * 
     * @param detallePedido El detalle de pedido a eliminar.
     */
    @Override
    public void borrar(DetallePedido detallePedido) {
        detallePedido.marcarInactivo();
        detallePedidoRepository.save(detallePedido);
    }

    /**
     * Verifica si un detalle de pedido con el identificador dado existe y está activo.
     * 
     * @param id El identificador del detalle de pedido.
     * @return true si el detalle de pedido existe y está activo, false en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return detallePedidoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta detalles activos
    }
}
