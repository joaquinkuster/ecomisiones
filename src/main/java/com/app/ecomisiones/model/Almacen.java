package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un almacén dentro de la base de datos.
 * Un almacén tiene una dirección, una ciudad asociada y un estado de baja.
 * Se utiliza en la gestión de almacenes en la aplicación.
 */
@Entity
@Table(name = "almacenes")
@Getter @Setter
@NoArgsConstructor
public class Almacen {

    /**
     * Identificador único del almacén.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Dirección del almacén. No puede ser nula y tiene una longitud máxima de 150 caracteres.
     */
    @Column(name = "direccion", nullable = false, length = 150)
    private String direccion;

    /**
     * Ciudad asociada al almacén. Es una relación muchos a uno con la entidad Ciudad.
     * No puede ser nula.
     */
    @ManyToOne
    @JoinColumn(name = "id_ciudad", nullable = false)
    private Ciudad ciudad;

    /**
     * Indica si el almacén está inactivo o dado de baja. Por defecto, es falso (activo).
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor de la clase Almacen que inicializa la dirección y la ciudad del almacén.
     * 
     * @param direccion Dirección del almacén.
     * @param ciudad Ciudad a la que pertenece el almacén.
     */
    public Almacen(String direccion, Ciudad ciudad) {
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    /**
     * Marca el almacén como inactivo (baja = true).
     * Este método puede ser utilizado para desactivar el almacén en la aplicación.
     */
    public void marcarInactivo(){
        baja = true;
    }

    /**
     * Verifica si el almacén está inactivo.
     * 
     * @return true si el almacén está inactivo (baja = true), false en caso contrario.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Representación en formato de cadena del almacén, que incluye la dirección,
     * la ciudad y la provincia asociada a la ciudad.
     * 
     * @return Una cadena con la dirección del almacén, ciudad y provincia.
     */
    @Override
    public String toString(){
        return direccion + " (" + ciudad + ", " + ciudad.getProvincia() + ")";
    }
}
