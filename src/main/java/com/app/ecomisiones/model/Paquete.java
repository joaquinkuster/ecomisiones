package com.app.ecomisiones.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Paquete extends Mercaderia{
    
    @OneToMany(mappedBy = "paquete", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Setter(AccessLevel.NONE) // Desactiva el setter para este atributo
    private Set<DetallePaquete> detalles = new HashSet<>();

    public Paquete(String nombre, String descripcion, float descuento) {
        super(nombre, descripcion, descuento);
    }

    public double calcularTotal(){
        double total = 0;
        Set<DetallePaquete> activos = getDetalles();
        for (DetallePaquete detalle : activos) {
            total += detalle.getCantidad() * detalle.getProducto().getPrecioConDescuento();
        }
        return total;
    }

}
