package com.app.ecomisiones.service.DetallePaquete;

import com.app.ecomisiones.model.DetallePaquete;
import com.app.ecomisiones.repository.DetallePaqueteRepository;
import com.app.ecomisiones.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePaqueteServiceImpl implements DetallePaqueteService, CrudService<DetallePaquete> {

    @Autowired
    private DetallePaqueteRepository detallePaqueteRepository;

    @Override
    public DetallePaquete guardar(DetallePaquete detallePaquete) {
        return detallePaqueteRepository.save(detallePaquete);
    }

    @Override
    public Optional<DetallePaquete> buscarPorId(Integer id) {
        return detallePaqueteRepository.findById(id)
                .filter(detallePaquete -> !detallePaquete.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<DetallePaquete> obtenerTodo() {
        return detallePaqueteRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public DetallePaquete modificar(DetallePaquete detallePaquete) {
        // Otros campos actualizables
        return detallePaqueteRepository.save(detallePaquete);
    }

    @Override
    public void borrar(DetallePaquete detallePaquete) {
        detallePaquete.marcarInactivo();
        detallePaqueteRepository.save(detallePaquete);
    }

    @Override
    public boolean existePorId(Integer id) {
        return detallePaqueteRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
