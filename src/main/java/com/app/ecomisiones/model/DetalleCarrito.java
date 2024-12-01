package com.app.ecomisiones.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalles_carrito")
@Getter @Setter
@NoArgsConstructor
public class DetalleCarrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable =  false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "id_carrito", nullable =  false)
    private Carrito carrito;

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public DetalleCarrito(int cantidad, Producto producto, Carrito carrito){
        this.cantidad = cantidad;
        this.producto = producto;
        this.carrito = carrito;
    }

    public void marcarInactivo(){
        baja = true;
    }

    public boolean esInactivo() {
        return baja;
    }
}
