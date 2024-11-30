package com.app.ecomisiones.model;

import java.io.ByteArrayInputStream;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "imagenes")
@Getter @Setter
@NoArgsConstructor
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "imagen", nullable = false)
    private byte[] imagen;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable =  false)
    private Producto producto;
    
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Imagen(byte[] imagen, Producto producto) {
        this.imagen = imagen;
        this.producto = producto;
    }

    public boolean esInactivo() {
        return baja;
    }

    @Override
    public String toString(){
        return imagen.toString();
    }
}