package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entidad que representa un carrito de compras en el sistema.
 * Un carrito contiene un conjunto de detalles de productos (DetalleCarrito) 
 * asociados a un usuario, y tiene un estado de "baja" para indicar si está activo o inactivo.
 */
@Entity
@Table(name = "carritos")
@Getter
@Setter
@NoArgsConstructor
public class Carrito {

    /**
     * Identificador único del carrito.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Usuario asociado al carrito. Es una relación uno a uno con la entidad Usuario.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    /**
     * Conjunto de detalles del carrito, representados por la entidad DetalleCarrito.
     * Este conjunto se mapea con la propiedad "carrito" de la entidad DetalleCarrito.
     * La propiedad no tiene setter, ya que se controla mediante los métodos de la propia clase.
     */
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE) // Desactiva el setter para este atributo
    private Set<DetalleCarrito> detalles = new HashSet<>();

    /**
     * Indica si el carrito está inactivo. El valor por defecto es falso (activo).
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor para crear un carrito con el usuario asociado.
     * 
     * @param usuario Usuario al que pertenece el carrito.
     */
    public Carrito(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Marca el carrito como inactivo (baja = true).
     * Este método se utiliza para desactivar el carrito.
     */
    public void marcarInactivo() {
        baja = true;
    }

    /**
     * Verifica si el carrito está inactivo.
     * 
     * @return true si el carrito está inactivo (baja = true), false en caso contrario.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Obtiene los detalles del carrito que no están inactivos.
     * 
     * @return Un conjunto de detalles activos (no inactivos) del carrito.
     */
    public Set<DetalleCarrito> getDetalles() {
        return detalles.stream()
                .filter(detalle -> !detalle.esInactivo())  // Filtra los detalles activos
                .collect(Collectors.toSet());
    }

    /**
     * Agrega un detalle al carrito.
     * 
     * @param detalle DetalleCarrito a agregar al carrito.
     */
    public void agregarDetalle(DetalleCarrito detalle) {
        detalles.add(detalle);
    }

    /**
     * Calcula el total de los productos en el carrito, teniendo en cuenta 
     * las cantidades y los precios con descuento.
     * 
     * @return El total de la compra en el carrito.
     */
    public double calcularTotal() {
        double total = 0;
        Set<DetalleCarrito> activos = getDetalles();  // Obtiene los detalles activos
        for (DetalleCarrito detalle : activos) {
            total += detalle.getCantidad() * detalle.getProducto().getPrecioConDescuento();  // Suma el precio de cada detalle
        }
        return total;
    }
}
