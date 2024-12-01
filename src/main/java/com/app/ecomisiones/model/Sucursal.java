package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sucursales")
@Getter @Setter
@NoArgsConstructor
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "direccion", nullable = false, length = 150)
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable =  false)
    private Ciudad ciudad;

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Sucursal(String direccion, Ciudad ciudad) {
        this.direccion = direccion;
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
        return direccion + " (" + ciudad + ", " + ciudad.getProvincia() + ")";
    }
}