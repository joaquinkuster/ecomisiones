package com.app.ecomisiones.model;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Getter @Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private int id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL)
    private Set<Categoria> subcategorias = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "id_padre", nullable =  true)
    private Categoria padre;

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public boolean esInactivo() {
        return baja;
    }

    @Override
    public String toString (){
        return nombre;
    }
}