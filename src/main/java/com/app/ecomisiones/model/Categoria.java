package com.app.ecomisiones.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa una categoría de productos o servicios en el sistema.
 * Las categorías pueden tener subcategorías y un padre, formando una jerarquía.
 */
@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    /**
     * Identificador único de la categoría.
     * Este valor es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)  // Desactiva el setter para este atributo
    private int id;

    /**
     * Nombre de la categoría.
     * No puede ser nulo y su longitud máxima es de 50 caracteres.
     */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /**
     * Descripción de la categoría.
     * No puede ser nula y su longitud máxima es de 500 caracteres.
     */
    @Column(name = "descripcion", nullable = false, length = 500)
    private String descripcion;

    /**
     * Conjunto de subcategorías asociadas a esta categoría.
     * La relación es de uno a muchos, donde una categoría puede tener muchas subcategorías.
     * Cada subcategoría tiene una referencia al campo "padre" que indica su categoría principal.
     */
    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL)
    private Set<Categoria> subcategorias = new HashSet<>();

    /**
     * Categoría padre de la categoría actual.
     * Una categoría puede tener una categoría principal, formando una jerarquía de categorías.
     * Esta relación es opcional, ya que la categoría principal puede ser nula en algunas circunstancias.
     */
    @ManyToOne
    @JoinColumn(name = "id_padre", nullable = true)
    private Categoria padre;

    /**
     * Indica si la categoría está inactiva (baja).
     * El valor por defecto es falso (activo).
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor para crear una categoría con el nombre y descripción proporcionados.
     * 
     * @param nombre Nombre de la categoría.
     * @param descripcion Descripción de la categoría.
     */
    public Categoria(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * Verifica si la categoría está inactiva (baja).
     * 
     * @return true si la categoría está inactiva, false si está activa.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Representación en forma de cadena de la categoría.
     * Se utiliza para mostrar el nombre de la categoría en las vistas o en la interfaz de usuario.
     * 
     * @return El nombre de la categoría.
     */
    @Override
    public String toString() {
        return nombre;
    }
}
