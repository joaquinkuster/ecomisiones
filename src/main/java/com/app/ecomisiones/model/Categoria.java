package com.app.ecomisiones.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categorias")
@Getter @Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @OneToMany(mappedBy = "padre")
    private List<Categoria> subcategorias;

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    @ManyToOne
    private Categoria padre;

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Categoria(String nombre, String descripcion, Categoria padre) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.padre = padre;
    }

    public boolean esInactivo() {
        return baja;
    }
}