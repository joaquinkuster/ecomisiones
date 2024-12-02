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
@Table(name = "detalles_paquete")
@Getter @Setter
@NoArgsConstructor
public class DetallePaquete {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "id_paquete", nullable =  false)
    private Paquete paquete;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable =  false)
    private Producto producto;
    
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public DetallePaquete(int cantidad, Paquete paquete, Producto producto) {
        this.cantidad = cantidad;
        this.paquete = paquete;
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
        return paquete + " ha agregado " + producto;
    }

}
