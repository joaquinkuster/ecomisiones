package com.app.ecomisiones.service.Pedido;

import java.util.List;

import com.app.ecomisiones.model.Pedido;
import com.app.ecomisiones.model.Usuario;

public interface PedidoService {

    List<Pedido> buscarPorComprador(Usuario usuario);

}
