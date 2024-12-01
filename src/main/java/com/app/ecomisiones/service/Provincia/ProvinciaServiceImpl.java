package com.app.ecomisiones.service.Provincia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Ciudad;
import com.app.ecomisiones.model.Provincia;
import com.app.ecomisiones.repository.CiudadRepository;
import com.app.ecomisiones.repository.ProvinciaRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinciaServiceImpl implements ProvinciaService, CrudService<Provincia> {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Override
    public Provincia guardar(Provincia provincia) {
        return provinciaRepository.save(provincia);
    }

    @Override
    public Optional<Provincia> buscarPorId(Integer id) {
        return provinciaRepository.findById(id)
                .filter(provincia -> !provincia.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<Provincia> obtenerTodo() {
        return provinciaRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public Provincia modificar(Provincia provincia) {
        // Otros campos actualizables
        return provinciaRepository.save(provincia);
    }

    @Override
    public void borrar(Provincia provincia) {
        provinciaRepository.delete(provincia);
    }

    @Override
    public boolean existePorId(Integer id) {
        return provinciaRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
