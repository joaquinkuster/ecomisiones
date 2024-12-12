package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un detalle de un carrito de compras.
 * Cada detalle corresponde a un producto específico en un carrito y su cantidad.
 */
@Entity
@Table(name = "detalles_carrito")
@Getter
@Setter
@NoArgsConstructor
public class DetalleCarrito {

    /**
     * Identificador único del detalle de carrito.
     * Este valor es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Cantidad del producto en el carrito.
     * Este valor no puede ser nulo.
     */
    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    /**
     * Producto asociado a este detalle de carrito.
     * La relación es de muchos a uno, donde un detalle pertenece a un producto.
     * Esta relación no puede ser nula.
     */
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    /**
     * Carrito al que pertenece este detalle.
     * La relación es de muchos a uno, donde un carrito puede tener varios detalles.
     * Esta relación no puede ser nula.
     */
    @ManyToOne
    @JoinColumn(name = "id_carrito", nullable = false)
    private Carrito carrito;

    /**
     * Indica si el detalle del carrito está inactivo (baja).
     * El valor por defecto es falso (activo).
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor para crear un detalle de carrito con la cantidad, producto y carrito proporcionados.
     *
     * @param cantidad Cantidad del producto en el carrito.
     * @param producto Producto asociado a este detalle.
     * @param carrito Carrito al que pertenece este detalle.
     */
    public DetalleCarrito(int cantidad, Producto producto, Carrito carrito) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.carrito = carrito;
    }

    /**
     * Marca este detalle de carrito como inactivo (baja).
     * Este método cambia el estado del detalle para indicar que ya no está activo.
     */
    public void marcarInactivo() {
        baja = true;
    }

    /**
     * Verifica si el detalle de carrito está inactivo (baja).
     *
     * @return true si el detalle está inactivo, false si está activo.
     */
    public boolean esInactivo() {
        return baja;
    }
}
