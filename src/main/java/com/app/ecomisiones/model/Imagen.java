package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una imagen asociada a un producto.
 * Cada imagen está vinculada a un producto específico y puede ser marcada como inactiva (baja).
 */
@Entity
@Table(name = "imagenes")
@Getter
@Setter
@NoArgsConstructor
public class Imagen {

    /**
     * Identificador único de la imagen.
     * Este valor es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Contenido de la imagen en formato de bytes.
     * Este valor no puede ser nulo.
     */
    @Column(name = "imagen", nullable = false)
    private byte[] imagen;

    /**
     * Producto asociado a esta imagen.
     * La relación es de muchos a uno, donde una imagen pertenece a un solo producto.
     * Esta relación no puede ser nula.
     */
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    /**
     * Indica si la imagen está inactiva (baja).
     * El valor por defecto es falso (activo).
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor para crear una imagen asociada a un producto específico.
     * 
     * @param imagen Contenido de la imagen en formato byte[].
     * @param producto Producto al que se asocia la imagen.
     */
    public Imagen(byte[] imagen, Producto producto) {
        this.imagen = imagen;
        this.producto = producto;
    }

    /**
     * Marca esta imagen como inactiva (baja).
     * Este método cambia el estado de la imagen a baja.
     */
    public void marcarInactivo() {
        baja = true;
    }

    /**
     * Verifica si la imagen está inactiva (baja).
     *
     * @return true si la imagen está inactiva, false si está activa.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Representación en cadena de caracteres de la imagen.
     * Devuelve la representación en cadena del contenido de la imagen (como un array de bytes).
     * 
     * @return Representación en cadena del contenido de la imagen.
     */
    @Override
    public String toString() {
        return imagen.toString();
    }
}
