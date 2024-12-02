package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalles_compra")
@Getter @Setter
@NoArgsConstructor
public class DetalleCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "id_compra", nullable =  false)
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable =  false)
    private Producto producto;
    
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public DetalleCompra(int cantidad, Compra compra, Producto producto) {
        this.cantidad = cantidad;
        this.compra = compra;
        this.producto = producto;
    }

    public void marcarInactivo(){
        baja = true;
    }

    public boolean esInactivo() {
        return baja;
    }

    @Override
    public String toString(){
        return compra + " ha agregado " + producto;
    }
}