package com.app.ecomisiones.repository;

import com.app.ecomisiones.model.Pedido;
import com.app.ecomisiones.model.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByBajaFalse();
    List<Pedido> findByComprador(Usuario usuario);

}
