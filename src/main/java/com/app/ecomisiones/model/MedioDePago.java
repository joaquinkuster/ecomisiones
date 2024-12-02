package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mediosdepago")
@Getter @Setter
@NoArgsConstructor
public class MedioDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public MedioDePago(String nombre) {
        this.nombre = nombre;
    }

    public void marcarInactivo(){
        baja = true;
    }

    public boolean esInactivo() {
        return baja;
    }

    @Override
    public String toString(){
        return nombre;
    }
}