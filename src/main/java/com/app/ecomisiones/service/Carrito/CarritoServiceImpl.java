package com.app.ecomisiones.service.Carrito;

import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.CarritoRepository;
import com.app.ecomisiones.service.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de Carrito, que gestiona las operaciones CRUD
 * para el modelo Carrito. Esta clase maneja las operaciones básicas de creación,
 * modificación, eliminación y búsqueda de carritos.
 */
@Service
public class CarritoServiceImpl implements CarritoService, CrudService<Carrito> {

    @Autowired
    private CarritoRepository carritoRepository;

    /**
     * Guarda un carrito en la base de datos.
     *
     * @param carrito el carrito a guardar
     * @return el carrito guardado
     */
    @Override
    public Carrito guardar(Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    /**
     * Busca un carrito por su ID, excluyendo aquellos que están marcados como inactivos.
     *
     * @param id el ID del carrito
     * @return un Optional que contiene el carrito si se encuentra activo, o vacío si no existe
     */
    @Override
    public Optional<Carrito> buscarPorId(Integer id) {
        return carritoRepository.findById(id)
                .filter(carrito -> !carrito.esInactivo()); // Solo devuelve carritos activos
    }

    /**
     * Obtiene todos los carritos almacenados en la base de datos.
     *
     * @return una lista con todos los carritos
     */
    @Override
    public List<Carrito> obtenerTodo() {
        return carritoRepository.findAll(); // Obtiene todos los carritos
    }

    /**
     * Modifica un carrito existente.
     *
     * @param carrito el carrito con los datos actualizados
     * @return el carrito modificado
     */
    @Override
    public Carrito modificar(Carrito carrito) {
        return carritoRepository.save(carrito); // Guarda el carrito con los nuevos datos
    }

    /**
     * Marca un carrito como inactivo en lugar de eliminarlo físicamente de la base de datos.
     *
     * @param carrito el carrito a eliminar
     */
    @Override
    public void borrar(Carrito carrito) {
        carrito.marcarInactivo(); // Marca el carrito como inactivo
        carritoRepository.save(carrito); // Guarda el cambio
    }

    /**
     * Verifica si un carrito existe en la base de datos y está activo.
     *
     * @param id el ID del carrito
     * @return true si el carrito existe y está activo, false en caso contrario
     */
    @Override
    public boolean existePorId(Integer id) {
        return carritoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Verifica si el carrito existe y está activo
    }

    /**
     * Busca un carrito asociado a un usuario.
     *
     * @param usuario el usuario cuyo carrito se desea obtener
     * @return el carrito del usuario, si existe
     */
    @Override
    public Carrito buscarPorUsuario(Usuario usuario) {
        return carritoRepository.findByUsuario(usuario);
    }
}
