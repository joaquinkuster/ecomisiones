package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una ciudad dentro de una provincia.
 * Las ciudades están asociadas a una provincia específica y pueden ser marcadas como inactivas.
 */
@Entity
@Table(name = "ciudades")
@Getter
@Setter
@NoArgsConstructor
public class Ciudad {

    /**
     * Identificador único de la ciudad.
     * Este valor es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Nombre de la ciudad.
     * No puede ser nulo y su longitud máxima es de 50 caracteres.
     */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /**
     * Provincia a la que pertenece la ciudad.
     * La relación es de muchos a uno, donde muchas ciudades pueden pertenecer a una misma provincia.
     * Esta relación no puede ser nula.
     */
    @ManyToOne
    @JoinColumn(name = "id_provincia", nullable = false)
    private Provincia provincia;

    /**
     * Indica si la ciudad está inactiva (baja).
     * El valor por defecto es falso (activo).
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor para crear una ciudad con el nombre y la provincia proporcionados.
     *
     * @param nombre Nombre de la ciudad.
     * @param provincia Provincia a la que pertenece la ciudad.
     */
    public Ciudad(String nombre, Provincia provincia) {
        this.nombre = nombre;
        this.provincia = provincia;
    }

    /**
     * Marca la ciudad como inactiva (baja).
     * Este método cambia el estado de la ciudad para indicar que ya no está activa.
     */
    public void marcarInactivo() {
        baja = true;
    }

    /**
     * Verifica si la ciudad está inactiva (baja).
     *
     * @return true si la ciudad está inactiva, false si está activa.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Representación en forma de cadena de la ciudad.
     * Se utiliza para mostrar el nombre de la ciudad en las vistas o en la interfaz de usuario.
     *
     * @return El nombre de la ciudad.
     */
    @Override
    public String toString() {
        return nombre;
    }
}
