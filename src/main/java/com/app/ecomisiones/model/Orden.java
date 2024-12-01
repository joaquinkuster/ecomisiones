package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "ordenes")
@Getter @Setter
@NoArgsConstructor
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "total", nullable = false)
    private double total;

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion = LocalDate.now();

    @Column(name = "mediopago", nullable = false)
    private MedioDePago medioDePago;

    @ManyToOne
    @JoinColumn(name = "id_direccion")
    private Sucursal direccion;

    @ManyToOne
    @JoinColumn(name = "id_comprador", nullable = false)
    private Usuario comprador;

    @ManyToOne
    @JoinColumn(name = "id_vendedor", nullable = false)
    private Usuario vendedor;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL)
    private List<DetalleOrden> detalles;

    public Orden(MedioDePago medioDePago, Sucursal direccion, Usuario comprador, Usuario vendedor) {
        this.medioDePago = medioDePago; 
        this.direccion = direccion;
        this.comprador = comprador;
        this.vendedor = vendedor;
    }

    @Override
    public String toString(){
        return vendedor + " " + fechaCreacion.toString();
    }
}
