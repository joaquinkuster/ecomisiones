package com.app.ecomisiones.service.DetalleCarrito;

import com.app.ecomisiones.model.DetalleCarrito;
import com.app.ecomisiones.repository.DetalleCarritoRepository;
import com.app.ecomisiones.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleCarritoServiceImpl implements DetalleCarritoService, CrudService<DetalleCarrito> {

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository;

    @Override
    public DetalleCarrito guardar(DetalleCarrito detalleCarrito) {
        return detalleCarritoRepository.save(detalleCarrito);
    }

    @Override
    public Optional<DetalleCarrito> buscarPorId(Integer id) {
        return detalleCarritoRepository.findById(id)
                .filter(carrito -> !carrito.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<DetalleCarrito> obtenerTodo() {
        return detalleCarritoRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public DetalleCarrito modificar(DetalleCarrito detalleCarrito) {
        // Otros campos actualizables
        return detalleCarritoRepository.save(detalleCarrito);
    }

    @Override
    public void borrar(DetalleCarrito detalleCarrito) {
        detalleCarrito.marcarInactivo();
        detalleCarritoRepository.save(detalleCarrito);
    }

    @Override
    public boolean existePorId(Integer id) {
        return detalleCarritoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
