package com.app.ecomisiones.service.MedioDePago;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.MedioDePago;
import com.app.ecomisiones.repository.MedioDePagoRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class MedioDePagoServiceImpl implements MedioDePagoService, CrudService<MedioDePago> {

    @Autowired
    private MedioDePagoRepository medioDePagoRepository;

    @Override
    public MedioDePago guardar(MedioDePago medioDePago) {
        return medioDePagoRepository.save(medioDePago);
    }

    @Override
    public Optional<MedioDePago> buscarPorId(Integer id) {
        return medioDePagoRepository.findById(id)
                .filter(medioDePago -> !medioDePago.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<MedioDePago> obtenerTodo() {
        return medioDePagoRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public MedioDePago modificar(MedioDePago medioDePago) {
        // Otros campos actualizables
        return medioDePagoRepository.save(medioDePago);
    }

    @Override
    public void borrar(MedioDePago medioDePago) {
        medioDePagoRepository.delete(medioDePago);
    }

    @Override
    public boolean existePorId(Integer id) {
        return medioDePagoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
