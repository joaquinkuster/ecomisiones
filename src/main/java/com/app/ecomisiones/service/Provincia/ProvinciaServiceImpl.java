package com.app.ecomisiones.service.Provincia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Provincia;
import com.app.ecomisiones.repository.ProvinciaRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones sobre la entidad {@link Provincia}.
 * Esta clase proporciona las operaciones CRUD básicas (Crear, Leer, Actualizar, Eliminar)
 * para la entidad Provincia.
 */
@Service
public class ProvinciaServiceImpl implements ProvinciaService, CrudService<Provincia> {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    /**
     * Guarda una nueva provincia en la base de datos.
     * 
     * @param provincia La provincia a guardar.
     * @return La provincia guardada.
     */
    @Override
    public Provincia guardar(Provincia provincia) {
        return provinciaRepository.save(provincia);
    }

    /**
     * Busca una provincia por su identificador.
     * Solo devuelve provincias activas (no marcadas como inactivas).
     * 
     * @param id El identificador de la provincia.
     * @return Un {@link Optional} con la provincia encontrada, si está activa.
     */
    @Override
    public Optional<Provincia> buscarPorId(Integer id) {
        return provinciaRepository.findById(id)
                .filter(provincia -> !provincia.esInactivo()); // Solo devuelve provincias activas
    }

    /**
     * Obtiene todas las provincias activas.
     * 
     * @return Una lista de todas las provincias activas.
     */
    @Override
    public List<Provincia> obtenerTodo() {
        return provinciaRepository.findByBajaFalse(); // Solo devuelve provincias activas
    }

    /**
     * Modifica una provincia existente.
     * 
     * @param provincia La provincia con los datos actualizados.
     * @return La provincia modificada.
     */
    @Override
    public Provincia modificar(Provincia provincia) {
        // Otros campos actualizables
        return provinciaRepository.save(provincia);
    }

    /**
     * Elimina una provincia de la base de datos.
     * 
     * @param provincia La provincia a eliminar.
     */
    @Override
    public void borrar(Provincia provincia) {
        provinciaRepository.delete(provincia); // Elimina la provincia de la base de datos
    }

    /**
     * Verifica si una provincia con el identificador dado existe y está activa.
     * 
     * @param id El identificador de la provincia.
     * @return true si la provincia existe y está activa, false en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return provinciaRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta provincias activas
    }
}
