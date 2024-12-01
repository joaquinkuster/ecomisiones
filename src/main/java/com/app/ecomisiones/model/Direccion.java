package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "direcciones")
@Getter @Setter
@NoArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "direccion", nullable = false, length = 150)
    private String direccion;

    @Column(name = "postal", nullable = false)
    private int postal;

    @Column(name = "depto", nullable = true)
    private int depto;

    @Column(name = "predeterminado", nullable = false)
    private boolean predeterminado;

    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable =  false)
    private Ciudad ciudad;
    
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Direccion(String direccion, int postal, int depto, Ciudad ciudad) {
        this.direccion = direccion;
        this.postal = postal;
        this.depto = depto;
        this.ciudad = ciudad;
    }

    public void marcarInactivo(){
        baja = true;
    }

    public boolean esInactivo() {
        return baja;
    }

    @Override
    public String toString(){
        return direccion + " " + ciudad + ", " + ciudad.getProvincia();
    }
}