package com.app.ecomisiones.service.Sucursal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Sucursal;
import com.app.ecomisiones.repository.SucursalRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones sobre la entidad {@link Sucursal}.
 * Esta clase proporciona las operaciones CRUD básicas (Crear, Leer, Actualizar, Eliminar)
 * para la entidad Sucursal.
 */
@Service
public class SucursalServiceImpl implements SucursalService, CrudService<Sucursal> {

    @Autowired
    private SucursalRepository sucursalRepository;

    /**
     * Guarda una nueva sucursal en la base de datos.
     * 
     * @param sucursal La sucursal a guardar.
     * @return La sucursal guardada.
     */
    @Override
    public Sucursal guardar(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }

    /**
     * Busca una sucursal por su identificador.
     * Solo devuelve sucursales activas (no marcadas como inactivas).
     * 
     * @param id El identificador de la sucursal.
     * @return Un {@link Optional} con la sucursal encontrada, si está activa.
     */
    @Override
    public Optional<Sucursal> buscarPorId(Integer id) {
        return sucursalRepository.findById(id)
                .filter(sucursal -> !sucursal.esInactivo()); // Solo devuelve sucursales activas
    }

    /**
     * Obtiene todas las sucursales activas.
     * 
     * @return Una lista de todas las sucursales activas.
     */
    @Override
    public List<Sucursal> obtenerTodo() {
        return sucursalRepository.findByBajaFalse(); // Solo devuelve sucursales activas
    }

    /**
     * Modifica una sucursal existente.
     * 
     * @param sucursal La sucursal con los datos actualizados.
     * @return La sucursal modificada.
     */
    @Override
    public Sucursal modificar(Sucursal sucursal) {
        // Otros campos actualizables
        return sucursalRepository.save(sucursal);
    }

    /**
     * Elimina una sucursal de la base de datos.
     * 
     * @param sucursal La sucursal a eliminar.
     */
    @Override
    public void borrar(Sucursal sucursal) {
        sucursalRepository.delete(sucursal); // Elimina la sucursal de la base de datos
    }

    /**
     * Verifica si una sucursal con el identificador dado existe y está activa.
     * 
     * @param id El identificador de la sucursal.
     * @return true si la sucursal existe y está activa, false en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return sucursalRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta sucursales activas
    }
}
