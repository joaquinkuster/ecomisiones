package com.app.ecomisiones.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mercaderias")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter @Setter
@NoArgsConstructor
public class Mercaderia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "descuento", nullable = false)
    private float descuento;

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Mercaderia(String nombre, String descripcion, float descuento) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.descuento = descuento;
    }

    public void marcarInactivo(){
        baja = true;
    }
    
    public boolean esInactivo() {
        return baja;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
