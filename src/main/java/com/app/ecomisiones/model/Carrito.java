package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "carritos")
@Getter
@Setter
@NoArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE) // Desactiva el setter para este atributo
    private Set<DetalleCarrito> detalles = new HashSet<>();

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Carrito(Usuario usuario) {
        this.usuario = usuario;
    }

    public void marcarInactivo() {
        baja = true;
    }

    public boolean esInactivo() {
        return baja;
    }

    public Set<DetalleCarrito> getDetalles() {
        return detalles.stream()
                .filter(detalle -> !detalle.esInactivo())
                .collect(Collectors.toSet());
    }

    public void agregarDetalle(DetalleCarrito detalle) {
        detalles.add(detalle);
    }

    public double calcularTotal() {
        double total = 0;
        Set<DetalleCarrito> activos = getDetalles();
        for (DetalleCarrito detalle : activos) {
            if (detalle.getMercaderia() instanceof Producto producto) {
                total += detalle.getCantidad() * producto.getPrecioConDescuento();
            } else if (detalle.getMercaderia() instanceof Paquete paquete) {
                total += detalle.getCantidad() * paquete.calcularTotal();
            }
        }
        return total;
    }
}
