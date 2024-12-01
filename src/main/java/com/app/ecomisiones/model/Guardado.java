package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "guardados")
@Getter @Setter
@NoArgsConstructor
public class Guardado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable =  false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable =  false)
    private Producto producto;
    
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Guardado(Usuario usuario, Producto producto) {
        this.usuario = usuario;
        this.producto = producto;
    }

    public void marcarInactivo(){
        baja = true;
    }

    public boolean esInactivo() {
        return baja;
    }

    @Override
    public String toString(){
        return usuario + " ha guardado " + producto;
    }
}