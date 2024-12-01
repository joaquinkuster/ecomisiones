package com.app.ecomisiones.service.Ciudad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Ciudad;
import com.app.ecomisiones.repository.CiudadRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class CiudadServiceImpl implements CiudadService, CrudService<Ciudad> {

    @Autowired
    private CiudadRepository ciudadRepository;

    @Override
    public Ciudad guardar(Ciudad ciudad) {
        return ciudadRepository.save(ciudad);
    }

    @Override
    public Optional<Ciudad> buscarPorId(Integer id) {
        return ciudadRepository.findById(id)
                .filter(ciudad -> !ciudad.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<Ciudad> obtenerTodo() {
        return ciudadRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public Ciudad modificar(Ciudad ciudad) {
        // Otros campos actualizables
        return ciudadRepository.save(ciudad);
    }

    @Override
    public void borrar(Ciudad ciudad) {
        ciudadRepository.delete(ciudad);
    }

    @Override
    public boolean existePorId(Integer id) {
        return ciudadRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
