package com.app.ecomisiones.model;

import java.util.HashSet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Entity
@Table(name = "productos")
@Getter @Setter
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

    @Column(name = "stock", nullable = false)
    private int stock;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable =  false)
    private Usuario vendedor;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private Set<Imagen> imagenes = new HashSet<>();

    public Producto(String nombre, String descripcion, float precio, float descuento, int stock, Usuario vendedor, Categoria categoria){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.descuento = descuento;
        this.stock = stock;
        this.vendedor = vendedor;
        this.categoria = categoria;
    }

    public void marcarInactivo(){
        baja = true;
    }
    
    public boolean esInactivo() {
        return baja;
    }

    public float getPrecioConDescuento(){
        return precio * (1 - descuento/100);
    }

    @Override
    public String toString() {
        return nombre;
    }
}