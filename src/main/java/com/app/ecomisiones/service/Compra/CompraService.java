package com.app.ecomisiones.service.Compra;

import java.util.List;

import com.app.ecomisiones.model.Compra;
import com.app.ecomisiones.model.Usuario;

public interface CompraService {

    List<Compra> buscarPorComprador(Usuario usuario);

}
