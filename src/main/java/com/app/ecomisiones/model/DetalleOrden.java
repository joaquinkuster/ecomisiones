package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalles_orden")
@Getter @Setter
@NoArgsConstructor
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "id_orden", nullable =  false)
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable =  false)
    private Producto producto;
    
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public DetalleOrden(int cantidad, Orden orden, Producto producto) {
        this.cantidad = cantidad;
        this.orden = orden;
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
        return orden + " ha agregado " + producto;
    }
}