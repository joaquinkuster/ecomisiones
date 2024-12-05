package com.app.ecomisiones.model;

import java.time.LocalDate;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "precio", nullable = false)
    private float precio;

    @Column(name = "descuento", nullable = false)
    private float descuento;

    @Column(name = "peso", nullable = false)
    private float peso;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion = LocalDate.now();

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "id_almacen", nullable = false)
    private Almacen almacen;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private Set<Imagen> imagenes = new HashSet<>();

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

    public float getPrecioConDescuento() {
        return getPrecio() * (1 - getDescuento() / 100);
    }

    public void marcarInactivo() {
        baja = true;
    }

    public boolean esInactivo() {
        return baja;
    }

    @Override
    public String toString() {
        return nombre;
    }
}