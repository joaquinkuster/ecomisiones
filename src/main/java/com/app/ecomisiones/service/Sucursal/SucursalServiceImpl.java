package com.app.ecomisiones.service.Sucursal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Ciudad;
import com.app.ecomisiones.model.Provincia;
import com.app.ecomisiones.model.Sucursal;
import com.app.ecomisiones.repository.CiudadRepository;
import com.app.ecomisiones.repository.ProvinciaRepository;
import com.app.ecomisiones.repository.SucursalRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class SucursalServiceImpl implements SucursalService, CrudService<Sucursal> {

    @Autowired
    private SucursalRepository sucursalRepository;

    @Override
    public Sucursal guardar(Sucursal sucursal) {
        return sucursalRepository.save(sucursal);
    }

    @Override
    public Optional<Sucursal> buscarPorId(Integer id) {
        return sucursalRepository.findById(id)
                .filter(sucursal -> !sucursal.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<Sucursal> obtenerTodo() {
        return sucursalRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public Sucursal modificar(Sucursal sucursal) {
        // Otros campos actualizables
        return sucursalRepository.save(sucursal);
    }

    @Override
    public void borrar(Sucursal sucursal) {
        sucursalRepository.delete(sucursal);
    }

    @Override
    public boolean existePorId(Integer id) {
        return sucursalRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }
}
