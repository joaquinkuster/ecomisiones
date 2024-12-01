package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ciudades")
@Getter @Setter
@NoArgsConstructor
public class Ciudad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_provincia", nullable =  false)
    private Provincia provincia;
    
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Ciudad(String nombre, Provincia provincia) {
        this.nombre = nombre;
        this.provincia = provincia;
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