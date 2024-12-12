package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa un detalle de un pedido.
 * Cada detalle de pedido contiene información sobre un producto específico,
 * su cantidad, peso y el medio de envío utilizado.
 */
@Entity
@Table(name = "detalles_pedido")
@Getter
@Setter
@NoArgsConstructor
public class DetallePedido {

    /**
     * Identificador único del detalle de pedido.
     * Este valor es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Cantidad de productos en el detalle de pedido.
     * Este valor no puede ser nulo.
     */
    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    /**
     * Peso total del producto en el detalle de pedido.
     * Este valor no puede ser nulo.
     */
    @Column(name = "peso", nullable = false)
    private float peso;

    /**
     * Medio de envío seleccionado para este detalle de pedido.
     * Esta relación es opcional y puede ser nula.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "medioDeEnvio", nullable = true)
    private MedioDeEnvio medioDeEnvio;

    /**
     * Pedido al que pertenece este detalle.
     * La relación es de muchos a uno, donde un pedido puede tener varios detalles.
     * Esta relación no puede ser nula.
     */
    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    /**
     * Producto asociado a este detalle de pedido.
     * La relación es de muchos a uno, donde un detalle de pedido pertenece a un producto.
     * Esta relación no puede ser nula.
     */
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    /**
     * Indica si el detalle de pedido está inactivo (baja).
     * El valor por defecto es falso (activo).
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor para crear un detalle de pedido con la cantidad, peso, medio de envío,
     * pedido y producto proporcionados.
     *
     * @param cantidad Cantidad del producto en el pedido.
     * @param peso Peso total del producto en el pedido.
     * @param medioDeEnvio Medio de envío seleccionado para este detalle.
     * @param pedido Pedido al que pertenece este detalle.
     * @param producto Producto asociado a este detalle de pedido.
     */
    public DetallePedido(int cantidad, float peso, MedioDeEnvio medioDeEnvio, Pedido pedido, Producto producto) {
        this.cantidad = cantidad;
        // Redondea el peso a dos decimales
        this.peso = (float) (Math.round(peso * 100.0) / 100.0);
        this.medioDeEnvio = medioDeEnvio;
        this.pedido = pedido;
        this.producto = producto;
    }

    /**
     * Marca este detalle de pedido como inactivo (baja).
     * Este método cambia el estado del detalle para indicar que ya no está activo.
     */
    public void marcarInactivo() {
        baja = true;
    }

    /**
     * Verifica si el detalle de pedido está inactivo (baja).
     *
     * @return true si el detalle está inactivo, false si está activo.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Representación en cadena de caracteres del detalle de pedido.
     * Devuelve una descripción del pedido y el producto.
     *
     * @return Descripción en formato cadena del detalle de pedido.
     */
    @Override
    public String toString() {
        return pedido + " ha agregado " + producto;
    }
}
