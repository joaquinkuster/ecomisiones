package com.app.ecomisiones.service.Producto;

import java.util.List;

import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Producto;

public interface ProductoService {

    List<Producto> buscarPorCategoria(Categoria categoria);
    List<Producto> buscarRecientes();
    List<Producto> buscarUltimosDisponibles();
    List<Producto> buscarMasVendidos();
}
