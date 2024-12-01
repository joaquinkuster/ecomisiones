package com.app.ecomisiones.service.OrdenCompra;

import com.app.ecomisiones.model.*;
import com.app.ecomisiones.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrdenCompraService {

    @Autowired
    private OrdenDeCompraRepository ordenDeCompraRepository;

    public void guardarOrdenDeCompra(Orden ordenDeCompra) {
        ordenDeCompraRepository.save(ordenDeCompra);
    }
}

