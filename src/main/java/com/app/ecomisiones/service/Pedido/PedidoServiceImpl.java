package com.app.ecomisiones.service.Pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ecomisiones.model.Pedido;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.PedidoRepository;
import com.app.ecomisiones.service.CrudService;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService, CrudService<Pedido> {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Optional<Pedido> buscarPorId(Integer id) {
        return pedidoRepository.findById(id)
                .filter(pedido -> !pedido.esInactivo()); // Solo devuelve categorias activas
    }

    @Override
    public List<Pedido> obtenerTodo() {
        return pedidoRepository.findByBajaFalse(); // Solo devuelve categorias activas
    }

    @Override
    public Pedido modificar(Pedido pedido) {
        // Otros campos actualizables
        return pedidoRepository.save(pedido);
    }

    @Override
    public void borrar(Pedido pedido) {
        pedido.marcarInactivo();
        pedidoRepository.save(pedido);
    }

    @Override
    public boolean existePorId(Integer id) {
        return pedidoRepository.existsById(id) &&
                buscarPorId(id).isPresent(); // Solo cuenta usuarios activos
    }

    @Override
    public List<Pedido> buscarPorComprador(Usuario usuario) {
        return pedidoRepository.findByComprador(usuario); // Solo devuelve categorias activas
    }
}
