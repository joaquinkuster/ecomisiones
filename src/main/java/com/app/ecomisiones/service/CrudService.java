package com.app.ecomisiones.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

    /**
     * Guarda una entidad en el sistema.
     * @param entidad la entidad a guardar
     * @return la entidad guardada
     */
    T guardar(T entidad);

    /**
     * Encuentra una entidad por su ID.
     * @param id el ID de la entidad a buscar
     * @return un Optional con la entidad si se encuentra, o vacío si no existe
     */
    Optional<T> buscarPorId(Integer id);

    /**
     * Obtiene todas las entidades.
     * @return una lista de todas las entidades
     */
    List<T> obtenerTodo();

    /**
     * Actualiza una entidad existente.
     * @param entidad la nueva información de la entidad
     * @return la entidad actualizada
     */
    T modificar(T entidad);

    /**
     * Elimina una entidad por su ID.
     * @param entidad la entidad a borrar
     */
    void borrar(T entidad);

    /**
     * Verifica si una entidad existe por su ID.
     * @param id el ID de la entidad
     * @return true si existe, false en caso contrario
     */
    boolean existePorId(Integer id);
}
