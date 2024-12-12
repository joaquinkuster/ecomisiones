package com.app.ecomisiones.service.Imagen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Imagen;
import com.app.ecomisiones.repository.ImagenRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para gestionar las operaciones sobre la entidad {@link Imagen}.
 * Esta clase implementa las operaciones básicas de CRUD (Crear, Leer, Actualizar, Eliminar)
 * para la entidad Imagen.
 */
@Service
public class ImagenServiceImpl implements ImagenService, CrudService<Imagen> {

    @Autowired
    private ImagenRepository imagenRepository;

    /**
     * Guarda una nueva imagen en la base de datos.
     * 
     * @param imagen La imagen a guardar.
     * @return La imagen guardada.
     */
    @Override
    public Imagen guardar(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    /**
     * Busca una imagen por su identificador.
     * Solo devuelve imágenes activas (no marcadas como inactivas).
     * 
     * @param id El identificador de la imagen.
     * @return Un {@link Optional} con la imagen encontrada, si está activa.
     */
    @Override
    public Optional<Imagen> buscarPorId(Integer id) {
        return imagenRepository.findById(id)
                .filter(imagen -> !imagen.esInactivo()); // Solo devuelve imágenes activas
    }

    /**
     * Obtiene todas las imágenes.
     * 
     * @return Una lista de todas las imágenes (activas o inactivas).
     */
    @Override
    public List<Imagen> obtenerTodo() {
        return imagenRepository.findAll(); // Devuelve todas las imágenes
    }

    /**
     * Modifica una imagen existente.
     * 
     * @param imagen La imagen con los datos actualizados.
     * @return La imagen modificada.
     */
    @Override
    public Imagen modificar(Imagen imagen) {
        // Otros campos actualizables
        return imagenRepository.save(imagen);
    }

    /**
     * Elimina una imagen de la base de datos.
     * 
     * @param imagen La imagen a eliminar.
     */
    @Override
    public void borrar(Imagen imagen) {
        imagenRepository.delete(imagen); // Elimina la imagen físicamente
    }

    /**
     * Verifica si una imagen con el identificador dado existe y está activa.
     * 
     * @param id El identificador de la imagen.
     * @return true si la imagen existe y está activa, false en caso contrario.
     */
    @Override
    public boolean existePorId(Integer id) {
        return imagenRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta imágenes activas
    }
}
