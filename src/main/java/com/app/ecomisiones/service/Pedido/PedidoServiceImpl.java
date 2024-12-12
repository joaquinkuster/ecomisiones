package com.app.ecomisiones.service.Pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Pedido;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.PedidoRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones sobre la entidad {@link Pedido}.
 * Esta clase proporciona las operaciones CRUD básicas (Crear, Leer, Actualizar, Eliminar)
 * para la entidad Pedido.
 */
@Service
public class PedidoServiceImpl implements PedidoService, CrudService<Pedido> {

    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * Guarda un nuevo pedido en la base de datos.
     * 
     * @param pedido El pedido a guardar.
     * @return El pedido guardado.
     */
    @Override
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    /**
     * Busca un pedido por su identificador.
     * Solo devuelve pedidos activos (no marcados como inactivos).
     * 
     * @param id El identificador del pedido.
     * @return Un {@link Optional} con el pedido encontrado, si está activo.
     */
    @Override
    public Optional<Pedido> buscarPorId(Integer id) {
        return pedidoRepository.findById(id)
                .filter(pedido -> !pedido.esInactivo()); // Solo devuelve pedidos activos
    }

    /**
     * Obtiene todos los pedidos activos.
     * 
     * @return Una lista de todos los pedidos activos.
     */
    @Override
    public List<Pedido> obtenerTodo() {
        return pedidoRepository.findByBajaFalse(); // Solo devuelve pedidos activos
    }

    /**
     * Modifica un pedido existente.
     * 
     * @param pedido El pedido con los datos actualizados.
     * @return El pedido modificado.
     */
    @Override
    public Pedido modificar(Pedido pedido) {
        return pedidoRepository.save(pedido); // Guarda el pedido modificado
    }

    /**
     * Marca un pedido como inactivo, en lugar de eliminarlo de la base de datos.
     * 
     * @param pedido El pedido a marcar como inactivo.
     */
    @Override
    public void borrar(Pedido pedido) {
        pedido.marcarInactivo(); // Marca el pedido como inactivo
        pedidoRepository.save(pedido); // Guarda el pedido marcado como inactivo
    }

    /**
     * Verifica si un pedido con el identificador dado existe y está activo.
     * 
     * @param id El identificador del pedido.
     * @return true si el pedido existe y está activo, false en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return pedidoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta pedidos activos
    }

    /**
     * Busca los pedidos realizados por un comprador específico.
     * 
     * @param usuario El usuario que realizó los pedidos.
     * @return Una lista de pedidos realizados por el usuario.
     */
    @Override
    public List<Pedido> buscarPorComprador(Usuario usuario) {
        return pedidoRepository.findByComprador(usuario); // Devuelve los pedidos realizados por el comprador
    }
}
