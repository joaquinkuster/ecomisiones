package com.app.ecomisiones.service.DetallePedido;

import com.app.ecomisiones.model.DetallePedido;
import com.app.ecomisiones.repository.DetallePedidoRepository;
import com.app.ecomisiones.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService, CrudService<DetallePedido> {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Override
    public DetallePedido guardar(DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public Optional<DetallePedido> buscarPorId(Integer id) {
        return detallePedidoRepository.findById(id)
                .filter(detallePedido -> !detallePedido.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<DetallePedido> obtenerTodo() {
        return detallePedidoRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public DetallePedido modificar(DetallePedido detallePedido) {
        // Otros campos actualizables
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public void borrar(DetallePedido detallePedido) {
        detallePedido.marcarInactivo();
        detallePedidoRepository.save(detallePedido);
    }

    @Override
    public boolean existePorId(Integer id) {
        return detallePedidoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
