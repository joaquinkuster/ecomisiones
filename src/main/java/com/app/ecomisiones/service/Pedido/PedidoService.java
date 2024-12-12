package com.app.ecomisiones.service.Pedido;

import java.util.List;

import com.app.ecomisiones.model.Pedido;
import com.app.ecomisiones.model.Usuario;

/**
 * Servicio para gestionar las operaciones relacionadas con los pedidos.
 */
public interface PedidoService {
    
    /**
     * Busca los pedidos realizados por un comprador específico.
     * 
     * @param usuario el usuario comprador cuyos pedidos se desean consultar
     * @return una lista de pedidos realizados por el usuario especificado
     */
    List<Pedido> buscarPorComprador(Usuario usuario);

}
