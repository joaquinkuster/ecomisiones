package com.app.ecomisiones.service.Carrito;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.CarritoRepository;
import com.app.ecomisiones.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarritoServiceImpl implements CarritoService, CrudService<Carrito> {

    @Autowired
    private CarritoRepository carritoRepository;

    @Override
    public Carrito guardar(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Override
    public Optional<Carrito> buscarPorId(Integer id) {
        return carritoRepository.findById(id)
                .filter(carrito -> !carrito.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<Carrito> obtenerTodo() {
        return carritoRepository.findAll(); // Solo devuelve categorias activas
    }

    @Override
    public Carrito modificar(Carrito carrito) {
        // Otros campos actualizables
        return carritoRepository.save(carrito);
    }

    @Override
    public void borrar(Carrito carrito) {
        carrito.marcarInactivo();
        carritoRepository.save(carrito);
    }

    @Override
    public boolean existePorId(Integer id) {
        return carritoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }

    @Override
    public Carrito buscarPorUsuario(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario);
    }
}
