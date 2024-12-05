package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalles_pedido")
@Getter @Setter
@NoArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "peso", nullable = false)
    private float peso;

    @Enumerated(EnumType.STRING)
    @Column(name = "medioDeEnvio", nullable = true)
    private MedioDeEnvio medioDeEnvio;

    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable =  false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_producto", nullable =  false)
    private Producto producto;
    
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public DetallePedido(int cantidad, float peso, MedioDeEnvio medioDeEnvio, Pedido pedido, Producto producto) {
        this.cantidad = cantidad;
        this.peso = peso;
        this.medioDeEnvio = medioDeEnvio;
        this.pedido = pedido;
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
        return pedido + " ha agregado " + producto;
    }
}