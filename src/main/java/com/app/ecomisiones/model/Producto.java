package com.app.ecomisiones.model;

import java.util.HashSet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Entity
@Getter @Setter
@NoArgsConstructor
public class Producto extends Mercaderia{

    @Column(name = "precio", nullable = false)
    private float precio;

    @Column(name = "stock", nullable = false)
    private int stock;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private Set<Imagen> imagenes = new HashSet<>();

    public Producto(String nombre, String descripcion, float precio, float descuento, int stock, Usuario vendedor, Categoria categoria){
        super(nombre, descripcion, descuento);
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    public float getPrecioConDescuento(){
        return precio * (1 - getDescuento()/100);
    }
}