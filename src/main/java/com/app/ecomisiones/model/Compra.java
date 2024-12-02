package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "compras")
@Getter @Setter
@NoArgsConstructor
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "total", nullable = false)
    private double total;

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion = LocalDate.now();

    @Column(name = "fechaLlegada", nullable = false)
    private LocalDate fechaLlegada;

    @ManyToOne
    @JoinColumn(name = "id_mediodepago")
    private MedioDePago medioDePago;

    @ManyToOne
    @JoinColumn(name = "id_sucursal")
    private Sucursal sucursal;

    @ManyToOne
    @JoinColumn(name = "id_comprador", nullable = false)
    private Usuario comprador;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE)
    private Set<DetalleCompra> detalles = new HashSet<>();

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Compra(double total, MedioDePago medioDePago, Sucursal sucursal, Usuario comprador, LocalDate fechaLlegada) {
        this.total = total;
        this.medioDePago = medioDePago; 
        this.sucursal = sucursal;
        this.comprador = comprador;
        this.fechaLlegada = fechaLlegada;
    }

    public void marcarInactivo(){
        baja = true;
    }
    
    public boolean esInactivo() {
        return baja;
    }

    public Set<DetalleCompra> getDetalles() {
        return detalles.stream()
                .filter(detalle -> !detalle.esInactivo())
                .collect(Collectors.toSet());
    }

    public void agregarDetalles(Set<DetalleCompra> detalles) {
        for (DetalleCompra detalleCompra : this.detalles) {
            detalles.add(detalleCompra);
        }
    }

    @Override
    public String toString(){
        return "Compra #" + id + " " + fechaCreacion.toString();
    }
}
