package com.app.ecomisiones.service.Producto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Producto;
import com.app.ecomisiones.repository.ProductoRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImpl implements ProductoService, CrudService<Producto>{

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .filter(Producto::esInactivo); // Solo devuelve categorias activas
    }

    @Override
    public List<Producto> obtenerTodo() {
        return productoRepository.findAll(); // Solo devuelve categorias activas
    }

    @Override
    public Producto modificar(Producto producto) {
        // Otros campos actualizables
        return productoRepository.save(producto);
    }

    @Override
    public void borrar(Producto producto) {
        productoRepository.delete(producto);
    }

    @Override
    public boolean existePorId(Integer id) {
        return productoRepository.existsById(id) &&
               buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
