package com.app.ecomisiones.service.DetalleCompra;

import com.app.ecomisiones.model.DetalleCompra;
import com.app.ecomisiones.repository.DetalleCompraRepository;
import com.app.ecomisiones.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleCompraServiceImpl implements DetalleCompraService, CrudService<DetalleCompra> {

    @Autowired
    private DetalleCompraRepository detalleOrdenRepository;

    @Override
    public DetalleCompra guardar(DetalleCompra detalleCompra) {
        return detalleOrdenRepository.save(detalleCompra);
    }

    @Override
    public Optional<DetalleCompra> buscarPorId(Integer id) {
        return detalleOrdenRepository.findById(id)
                .filter(carrito -> !carrito.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<DetalleCompra> obtenerTodo() {
        return detalleOrdenRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public DetalleCompra modificar(DetalleCompra detalleCompra) {
        // Otros campos actualizables
        return detalleOrdenRepository.save(detalleCompra);
    }

    @Override
    public void borrar(DetalleCompra detalleCompra) {
        detalleCompra.marcarInactivo();
        detalleOrdenRepository.save(detalleCompra);
    }

    @Override
    public boolean existePorId(Integer id) {
        return detalleOrdenRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
