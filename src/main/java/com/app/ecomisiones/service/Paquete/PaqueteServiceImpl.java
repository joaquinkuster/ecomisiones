package com.app.ecomisiones.service.Paquete;

import com.app.ecomisiones.model.Paquete;
import com.app.ecomisiones.repository.PaqueteRepository;
import com.app.ecomisiones.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaqueteServiceImpl implements PaqueteService, CrudService<Paquete> {

    @Autowired
    private PaqueteRepository paqueteRepository;

    @Override
    public Paquete guardar(Paquete paquete) {
        return paqueteRepository.save(paquete);
    }

    @Override
    public Optional<Paquete> buscarPorId(Integer id) {
        return paqueteRepository.findById(id)
                .filter(paquete -> !paquete.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<Paquete> obtenerTodo() {
        return paqueteRepository.findAll(); // Solo devuelve categorias activas
    }

    @Override
    public Paquete modificar(Paquete paquete) {
        // Otros campos actualizables
        return paqueteRepository.save(paquete);
    }

    @Override
    public void borrar(Paquete paquete) {
        paquete.marcarInactivo();
        paqueteRepository.save(paquete);
    }

    @Override
    public boolean existePorId(Integer id) {
        return paqueteRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
