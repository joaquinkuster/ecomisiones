package com.app.ecomisiones.service.Almacen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Almacen;
import com.app.ecomisiones.repository.AlmacenRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class AlmacenServiceImpl implements AlmacenService, CrudService<Almacen> {

    @Autowired
    private AlmacenRepository almacenRepository;

    @Override
    public Almacen guardar(Almacen almacen) {
        return almacenRepository.save(almacen);
    }

    @Override
    public Optional<Almacen> buscarPorId(Integer id) {
        return almacenRepository.findById(id)
                .filter(almacen -> !almacen.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<Almacen> obtenerTodo() {
        return almacenRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public Almacen modificar(Almacen almacen) {
        // Otros campos actualizables
        return almacenRepository.save(almacen);
    }

    @Override
    public void borrar(Almacen almacen) {
        almacenRepository.delete(almacen);
    }

    @Override
    public boolean existePorId(Integer id) {
        return almacenRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
