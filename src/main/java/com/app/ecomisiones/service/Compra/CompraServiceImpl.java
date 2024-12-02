package com.app.ecomisiones.service.Compra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Compra;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.CompraRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class CompraServiceImpl implements CompraService, CrudService<Compra> {

    @Autowired
    private CompraRepository ordenRepository;

    @Override
    public Compra guardar(Compra compra) {
        return ordenRepository.save(compra);
    }

    @Override
    public Optional<Compra> buscarPorId(Integer id) {
        return ordenRepository.findById(id)
                .filter(compra -> !compra.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<Compra> obtenerTodo() {
        return ordenRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public Compra modificar(Compra compra) {
        // Otros campos actualizables
        return ordenRepository.save(compra);
    }

    @Override
    public void borrar(Compra compra) {
        compra.marcarInactivo();
        ordenRepository.save(compra);
    }

    @Override
    public boolean existePorId(Integer id) {
        return ordenRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }

    @Override
    public List<Compra> buscarPorComprador(Usuario usuario) {
        return ordenRepository.findByComprador(usuario); // Solo devuelve categorias activas
    }
}
