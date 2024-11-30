package com.app.ecomisiones.service.Categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.repository.CategoriaRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaServiceImpl implements CategoriaService, CrudService<Categoria> {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public Categoria guardar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id)
                .filter(categoria -> !categoria.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<Categoria> obtenerTodo() {
        return categoriaRepository.findAll(); // Solo devuelve categorias activas
    }

    @Override
    public Categoria modificar(Categoria categoria) {
        // Otros campos actualizables
        return categoriaRepository.save(categoria);
    }

    @Override
    public void borrar(Categoria categoria) {
        categoriaRepository.delete(categoria);
    }

    @Override
    public boolean existePorId(Integer id) {
        return categoriaRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
