package com.app.ecomisiones.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un producto disponible en el sistema.
 * Contiene información sobre el producto como su nombre, descripción, precio, stock, entre otros,
 * así como las relaciones con otras entidades como categoría, almacén, imágenes y ventas.
 */
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    /**
     * Identificador único del producto.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Nombre del producto.
     */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /**
     * Descripción del producto.
     */
    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    /**
     * Precio base del producto.
     */
    @Column(name = "precio", nullable = false)
    private float precio;

    /**
     * Descuento aplicable al producto en porcentaje (0-100).
     */
    @Column(name = "descuento", nullable = false)
    private float descuento;

    /**
     * Peso del producto en unidades de medida específicas (por ejemplo, kilogramos).
     */
    @Column(name = "peso", nullable = false)
    private float peso;

    /**
     * Stock disponible del producto en el almacén.
     */
    @Column(name = "stock", nullable = false)
    private int stock;

    /**
     * Fecha de creación del producto. Se establece al momento actual por defecto.
     */
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion = LocalDate.now();

    /**
     * Indicador de si el producto está activo o inactivo. El valor predeterminado es "false", indicando que está activo.
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Categoría a la que pertenece el producto.
     */
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    /**
     * Almacén en el que se encuentra el producto.
     */
    @ManyToOne
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    /**
     * Conjunto de imágenes asociadas al producto.
     */
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private Set<Imagen> imagenes = new HashSet<>();

    /**
     * Conjunto de ventas (detalles de pedidos) asociados al producto.
     */
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private Set<DetallePedido> ventas = new HashSet<>();

    /**
     * Constructor para crear un producto con los parámetros proporcionados.
     *
     * @param nombre     Nombre del producto.
     * @param descripcion Descripción del producto.
     * @param precio     Precio base del producto.
     * @param descuento  Descuento aplicable al producto.
     * @param peso       Peso del producto.
     * @param stock      Cantidad disponible en stock.
     * @param categoria  Categoría a la que pertenece el producto.
     * @param almacen    Almacén en el que se encuentra el producto.
     */
    public Producto(String nombre, String descripcion, float precio, float descuento, float peso, int stock,
                    Categoria categoria, Almacen almacen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.descuento = descuento;
        this.peso = peso;
        this.stock = stock;
        this.categoria = categoria;
        this.almacen = almacen;
    }

    /**
     * Obtiene el precio del producto con el descuento aplicado.
     *
     * @return El precio con el descuento aplicado.
     */
    public float getPrecioConDescuento() {
        return getPrecio() * (1 - getDescuento() / 100);
    }

    /**
     * Marca el producto como inactivo, estableciendo el atributo 'baja' a true.
     */
    public void marcarInactivo() {
        baja = true;
    }

    /**
     * Verifica si el producto está inactivo.
     *
     * @return true si el producto está inactivo (baja = true), false si está activo.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Devuelve una representación en forma de cadena del producto.
     *
     * @return El nombre del producto.
     */
    @Override
    public String toString() {
        return nombre;
    }
}
