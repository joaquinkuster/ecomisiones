package com.app.ecomisiones.service.Ciudad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Ciudad;
import com.app.ecomisiones.repository.CiudadRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones sobre la entidad {@link Ciudad}.
 * Esta clase implementa las operaciones básicas de CRUD (Crear, Leer, Actualizar, Eliminar) 
 * para la entidad Ciudad.
 */
@Service
public class CiudadServiceImpl implements CiudadService, CrudService<Ciudad> {

    @Autowired
    private CiudadRepository ciudadRepository;

    /**
     * Guarda una nueva ciudad en la base de datos.
     * 
     * @param ciudad La ciudad a guardar.
     * @return La ciudad guardada.
     */
    @Override
    public Ciudad guardar(Ciudad ciudad) {
        return ciudadRepository.save(ciudad);
    }

    /**
     * Busca una ciudad por su identificador.
     * Solo devuelve ciudades activas (no marcadas como inactivas).
     * 
     * @param id El identificador de la ciudad.
     * @return Un Optional con la ciudad encontrada, si está activa.
     */
    @Override
    public Optional<Ciudad> buscarPorId(Integer id) {
        return ciudadRepository.findById(id)
                .filter(ciudad -> !ciudad.esInactivo()); // Solo devuelve ciudades activas
    }

    /**
     * Obtiene todas las ciudades activas.
     * 
     * @return Una lista de ciudades activas.
     */
    @Override
    public List<Ciudad> obtenerTodo() {
        return ciudadRepository.findByBajaFalse(); // Solo devuelve ciudades activas
    }

    /**
     * Modifica una ciudad existente.
     * 
     * @param ciudad La ciudad con los datos actualizados.
     * @return La ciudad modificada.
     */
    @Override
    public Ciudad modificar(Ciudad ciudad) {
        // Otros campos actualizables
        return ciudadRepository.save(ciudad);
    }

    /**
     * Elimina una ciudad. Esto no la elimina físicamente de la base de datos,
     * sino que la marca como inactiva.
     * 
     * @param ciudad La ciudad a eliminar.
     */
    @Override
    public void borrar(Ciudad ciudad) {
        ciudadRepository.delete(ciudad); // Se asume que la eliminación marca la ciudad como inactiva
    }

    /**
     * Verifica si una ciudad con el identificador dado existe y está activa.
     * 
     * @param id El identificador de la ciudad.
     * @return true si la ciudad existe y está activa, false en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return ciudadRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta ciudades activas
    }
}
