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
    private double precio;

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

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private Set<Imagen> imagenes = new HashSet<>();

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Producto(String nombre){
        this.nombre = nombre;
    }

    public boolean esInactivo() {
        return baja;
    }

    @Override
    public String toString() {
        return nombre;
    }
}