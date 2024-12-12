package com.app.ecomisiones.service.Categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.repository.CategoriaRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para manejar operaciones relacionadas con las categorías.
 * Proporciona operaciones básicas de CRUD (crear, leer, actualizar y eliminar) para la entidad {@link Categoria}.
 * Además, garantiza que solo se trabajen con categorías activas (no marcadas como bajas).
 */
@Service
public class CategoriaServiceImpl implements CategoriaService, CrudService<Categoria> {

    @Autowired
    private CategoriaRepository categoriaRepository;

    /**
     * Guarda una nueva categoría en la base de datos.
     * 
     * @param categoria La categoría a guardar.
     * @return La categoría guardada.
     */
    @Override
    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    /**
     * Busca una categoría por su ID. Solo devuelve categorías activas (no marcadas como baja).
     * 
     * @param id El ID de la categoría a buscar.
     * @return Un {@link Optional} que contiene la categoría si se encuentra activa.
     */
    @Override
    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id)
                .filter(categoria -> !categoria.esInactivo()); // Solo devuelve categorías activas
    }

    /**
     * Obtiene todas las categorías activas (no marcadas como baja).
     * 
     * @return Una lista de categorías activas.
     */
    @Override
    public List<Categoria> obtenerTodo() {
        return categoriaRepository.findByBajaFalse(); // Solo devuelve categorías activas
    }

    /**
     * Modifica una categoría existente.
     * 
     * @param categoria La categoría con los nuevos datos.
     * @return La categoría modificada.
     */
    @Override
    public Categoria modificar(Categoria categoria) {
        return categoriaRepository.save(categoria); // Otros campos actualizables
    }

    /**
     * Elimina una categoría. Se asume que la categoría no debe eliminarse físicamente,
     * sino marcarse como inactiva.
     * 
     * @param categoria La categoría a eliminar.
     */
    @Override
    public void borrar(Categoria categoria) {
        categoriaRepository.delete(categoria);
    }

    /**
     * Verifica si una categoría con el ID proporcionado existe y está activa.
     * 
     * @param id El ID de la categoría a verificar.
     * @return {@code true} si la categoría existe y está activa, {@code false} en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return categoriaRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta categorías activas
    }
}
