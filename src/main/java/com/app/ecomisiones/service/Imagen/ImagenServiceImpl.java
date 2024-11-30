package com.app.ecomisiones.service.Imagen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Imagen;
import com.app.ecomisiones.repository.ImagenRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class ImagenServiceImpl implements ImagenService, CrudService<Imagen> {

    @Autowired
    private ImagenRepository imagenRepository;

    @Override
    public Imagen guardar(Imagen imagen) {
        return imagenRepository.save(imagen);
    }

    @Override
    public Optional<Imagen> buscarPorId(Integer id) {
        return imagenRepository.findById(id)
                .filter(imagen -> !imagen.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<Imagen> obtenerTodo() {
        return imagenRepository.findAll(); // Solo devuelve categorias activas
    }

    @Override
    public Imagen modificar(Imagen imagen) {
        // Otros campos actualizables
        return imagenRepository.save(imagen);
    }

    @Override
    public void borrar(Imagen imagen) {
        imagenRepository.delete(imagen);
    }

    @Override
    public boolean existePorId(Integer id) {
        return imagenRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
