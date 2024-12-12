package com.app.ecomisiones.service.MedioDePago;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.MedioDePago;
import com.app.ecomisiones.repository.MedioDePagoRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones sobre la entidad {@link MedioDePago}.
 * Esta clase proporciona las operaciones CRUD básicas (Crear, Leer, Actualizar, Eliminar) 
 * para la entidad MedioDePago.
 */
@Service
public class MedioDePagoServiceImpl implements MedioDePagoService, CrudService<MedioDePago> {

    @Autowired
    private MedioDePagoRepository medioDePagoRepository;

    /**
     * Guarda un nuevo medio de pago en la base de datos.
     * 
     * @param medioDePago El medio de pago a guardar.
     * @return El medio de pago guardado.
     */
    @Override
    public MedioDePago guardar(MedioDePago medioDePago) {
        return medioDePagoRepository.save(medioDePago);
    }

    /**
     * Busca un medio de pago por su identificador.
     * Solo devuelve medios de pago activos (no marcados como inactivos).
     * 
     * @param id El identificador del medio de pago.
     * @return Un {@link Optional} con el medio de pago encontrado, si está activo.
     */
    @Override
    public Optional<MedioDePago> buscarPorId(Integer id) {
        return medioDePagoRepository.findById(id)
                .filter(medioDePago -> !medioDePago.esInactivo()); // Solo devuelve medios de pago activos
    }

    /**
     * Obtiene todos los medios de pago activos.
     * 
     * @return Una lista de todos los medios de pago activos.
     */
    @Override
    public List<MedioDePago> obtenerTodo() {
        return medioDePagoRepository.findByBajaFalse(); // Solo devuelve medios de pago activos
    }

    /**
     * Modifica un medio de pago existente.
     * 
     * @param medioDePago El medio de pago con los datos actualizados.
     * @return El medio de pago modificado.
     */
    @Override
    public MedioDePago modificar(MedioDePago medioDePago) {
        // Otros campos actualizables
        return medioDePagoRepository.save(medioDePago);
    }

    /**
     * Elimina un medio de pago de la base de datos.
     * 
     * @param medioDePago El medio de pago a eliminar.
     */
    @Override
    public void borrar(MedioDePago medioDePago) {
        medioDePagoRepository.delete(medioDePago); // Elimina el medio de pago de la base de datos
    }

    /**
     * Verifica si un medio de pago con el identificador dado existe y está activo.
     * 
     * @param id El identificador del medio de pago.
     * @return true si el medio de pago existe y está activo, false en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return medioDePagoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta medios de pago activos
    }
}
