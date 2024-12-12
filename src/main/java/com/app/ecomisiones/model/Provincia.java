package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa una provincia dentro del sistema.
 * Contiene el nombre de la provincia y un indicador de si está activa o inactiva.
 */
@Entity
@Table(name = "provincias")
@Getter
@Setter
@NoArgsConstructor
public class Provincia {

    /**
     * Identificador único de la provincia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Nombre de la provincia.
     */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    
    /**
     * Indicador de si la provincia está activa o inactiva. El valor predeterminado es "false", indicando que está activa.
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor para crear una provincia con el nombre proporcionado.
     *
     * @param nombre Nombre de la provincia.
     */
    public Provincia(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Marca la provincia como inactiva, estableciendo el atributo 'baja' a true.
     */
    public void marcarInactivo(){
        baja = true;
    }

    /**
     * Verifica si la provincia está inactiva.
     *
     * @return true si la provincia está inactiva (baja = true), false si está activa.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Devuelve una representación en forma de cadena del nombre de la provincia.
     *
     * @return El nombre de la provincia.
     */
    @Override
    public String toString(){
        return nombre;
    }
}
