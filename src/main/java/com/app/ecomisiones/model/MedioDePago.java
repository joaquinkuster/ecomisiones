package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un medio de pago utilizado en el sistema.
 * Contiene el nombre del medio de pago y su estado (activo/inactivo).
 */
@Entity
@Table(name = "mediosdepago")
@Getter @Setter
@NoArgsConstructor
public class MedioDePago {

    /**
     * Identificador único del medio de pago.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Nombre del medio de pago (por ejemplo, "Tarjeta de Crédito", "Efectivo", etc.).
     */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    
    /**
     * Indicador de si el medio de pago está activo o inactivo.
     * El valor predeterminado es "false", indicando que está activo.
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor para inicializar un medio de pago con un nombre específico.
     *
     * @param nombre El nombre del medio de pago.
     */
    public MedioDePago(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Marca el medio de pago como inactivo.
     * Establece el atributo 'baja' en verdadero.
     */
    public void marcarInactivo() {
        baja = true;
    }

    /**
     * Verifica si el medio de pago está inactivo.
     *
     * @return true si el medio de pago está inactivo (baja = true), false si está activo.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Devuelve una representación en forma de cadena del medio de pago.
     *
     * @return El nombre del medio de pago.
     */
    @Override
    public String toString() {
        return nombre;
    }
}
