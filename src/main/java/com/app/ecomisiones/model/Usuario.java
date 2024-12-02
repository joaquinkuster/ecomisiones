package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor
public class Usuario implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;

    @Column(name = "telefono", nullable = true, length = 50)
    private String telefono;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion = LocalDate.now();

    @OneToOne(mappedBy = "usuario")
    private Carrito carrito;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolUsuario rol = RolUsuario.Usuario;

    @ManyToOne
    @JoinColumn(name = "id_sucursal", nullable =  true)
    private Sucursal sucursalMasCercana;

    @OneToMany(mappedBy = "comprador", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Compra> compras = new HashSet<>();

    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    public Usuario(String nombre, String apellido, String correo, String telefono, String password){
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.password = password;
    }

    public boolean esInactivo() {
        return baja;
    }

    public String toString() {
        return nombre + " " + apellido; 
    }

    //Implementacion de UserDateils de Spring Security para manejo de sesion
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getUsername() {
        return correo;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void agregarCompra(Compra compra) {
        compras.add(compra);
    }
}