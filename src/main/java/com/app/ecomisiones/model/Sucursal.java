package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa una sucursal en el sistema.
 * Una sucursal tiene una dirección y está asociada a una ciudad.
 * Además, puede estar marcada como inactiva (baja).
 */
@Entity
@Table(name = "sucursales")
@Getter @Setter
@NoArgsConstructor
public class Sucursal {

    /**
     * Identificador único de la sucursal.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Dirección de la sucursal.
     * No puede ser nula y tiene un tamaño máximo de 150 caracteres.
     */
    @Column(name = "direccion", nullable = false, length = 150)
    private String direccion;

    /**
     * Ciudad a la que pertenece la sucursal.
     * Una sucursal está asociada a una ciudad, que no puede ser nula.
     */
    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    /**
     * Indica si la sucursal está activa o inactiva.
     * El valor por defecto es `false` (activa).
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor que permite crear una sucursal con dirección y ciudad.
     *
     * @param direccion Dirección de la sucursal.
     * @param ciudad Ciudad a la que pertenece la sucursal.
     */
    public Sucursal(String direccion, Ciudad ciudad) {
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    /**
     * Marca la sucursal como inactiva.
     */
    public void marcarInactivo() {
        baja = true;
    }

    /**
     * Verifica si la sucursal está inactiva.
     *
     * @return `true` si la sucursal está inactiva, `false` en caso contrario.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Devuelve una representación en cadena de la sucursal con su dirección y ciudad asociada.
     *
     * @return Una cadena que representa la sucursal con su dirección y ciudad.
     */
    @Override
    public String toString() {
        return direccion + " (" + ciudad + ", " + ciudad.getProvincia() + ")";
    }
}
