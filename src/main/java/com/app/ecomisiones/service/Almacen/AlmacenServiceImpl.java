package com.app.ecomisiones.service.Almacen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Almacen;
import com.app.ecomisiones.repository.AlmacenRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones relacionadas con los almacenes.
 * Extiende de {@link CrudService} para la funcionalidad CRUD básica.
 */
@Service
public class AlmacenServiceImpl implements AlmacenService, CrudService<Almacen> {

    @Autowired
    private AlmacenRepository almacenRepository;

    /**
     * Guarda un almacén en la base de datos.
     *
     * @param almacen El almacén que se desea guardar.
     * @return El almacén guardado.
     */
    @Override
    public Almacen guardar(Almacen almacen) {
        return almacenRepository.save(almacen);
    }

    /**
     * Busca un almacén por su ID, asegurándose de que esté activo.
     *
     * @param id El ID del almacén que se desea buscar.
     * @return Un {@link Optional} que contiene el almacén si está activo, de lo contrario está vacío.
     */
    @Override
    public Optional<Almacen> buscarPorId(Integer id) {
        return almacenRepository.findById(id)
                .filter(almacen -> !almacen.esInactivo()); // Solo devuelve almacenes activos
    }

    /**
     * Obtiene todos los almacenes activos.
     *
     * @return Una lista de almacenes activos.
     */
    @Override
    public List<Almacen> obtenerTodo() {
        return almacenRepository.findByBajaFalse(); // Solo devuelve almacenes activos
    }

    /**
     * Modifica un almacén existente.
     *
     * @param almacen El almacén con los datos modificados.
     * @return El almacén modificado.
     */
    @Override
    public Almacen modificar(Almacen almacen) {
        return almacenRepository.save(almacen); // Guarda los cambios realizados en el almacén
    }

    /**
     * Borra un almacén de la base de datos.
     *
     * @param almacen El almacén que se desea borrar.
     */
    @Override
    public void borrar(Almacen almacen) {
        almacenRepository.delete(almacen); // Elimina el almacén de la base de datos
    }

    /**
     * Verifica si un almacén con el ID especificado existe y está activo.
     *
     * @param id El ID del almacén que se desea verificar.
     * @return {@code true} si el almacén existe y está activo, {@code false} de lo contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return almacenRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Verifica si el almacén existe y está activo
    }
}
