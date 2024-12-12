package com.app.ecomisiones.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa a un usuario en el sistema.
 * Los usuarios tienen un nombre, apellido, correo electrónico, teléfono, contraseña, rol, carrito de compras,
 * sucursal más cercana y medio de pago predeterminado.
 * Además, implementa la interfaz {@link UserDetails} de Spring Security para la gestión de la autenticación y la autorización.
 */
@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor
public class Usuario implements UserDetails {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Nombre del usuario.
     * No puede ser nulo y tiene un tamaño máximo de 50 caracteres.
     */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /**
     * Apellido del usuario.
     * No puede ser nulo y tiene un tamaño máximo de 50 caracteres.
     */
    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    /**
     * Correo electrónico del usuario.
     * Debe ser único y no puede ser nulo.
     * Tiene un tamaño máximo de 150 caracteres.
     */
    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;

    /**
     * Teléfono del usuario.
     * Es opcional y tiene un tamaño máximo de 50 caracteres.
     */
    @Column(name = "telefono", nullable = true, length = 50)
    private String telefono;

    /**
     * Contraseña del usuario.
     * No puede ser nula.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Fecha de creación de la cuenta del usuario.
     * Por defecto es la fecha actual.
     */
    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fechaCreacion = LocalDate.now();

    /**
     * Carrito de compras del usuario.
     * Un usuario puede tener un único carrito, que está asociado a esta entidad.
     */
    @OneToOne(mappedBy = "usuario")
    private Carrito carrito;

    /**
     * Rol del usuario.
     * El valor predeterminado es {@link RolUsuario#Usuario}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolUsuario rol = RolUsuario.Usuario;

    /**
     * Sucursal más cercana del usuario.
     * Es opcional y puede ser nula.
     */
    @ManyToOne
    @JoinColumn(name = "id_sucursal", nullable = true)
    private Sucursal sucursalMasCercana;

    /**
     * Medio de pago predeterminado del usuario.
     * Es opcional y puede ser nulo.
     */
    @ManyToOne
    @JoinColumn(name = "id_medioDePago", nullable = true)
    private MedioDePago medioDePagoPredeterminado;

    /**
     * Conjunto de pedidos realizados por el usuario.
     * Los pedidos están asociados a este usuario como comprador.
     */
    @OneToMany(mappedBy = "comprador", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Pedido> compras = new HashSet<>();

    /**
     * Indica si la cuenta del usuario está activa o inactiva.
     * El valor por defecto es `false` (activa).
     */
    @Column(name = "baja", nullable = false)
    private Boolean baja = false;

    /**
     * Constructor para crear un usuario con los datos básicos.
     * @param nombre El nombre del usuario.
     * @param apellido El apellido del usuario.
     * @param correo El correo electrónico del usuario.
     * @param telefono El teléfono del usuario.
     * @param password La contraseña del usuario.
     */
    public Usuario(String nombre, String apellido, String correo, String telefono, String password) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.telefono = telefono;
        this.password = password;
    }

    /**
     * Verifica si el usuario está inactivo.
     * @return `true` si el usuario está inactivo (baja), `false` en caso contrario.
     */
    public boolean esInactivo() {
        return baja;
    }

    /**
     * Representa al usuario como una cadena de texto con su nombre y apellido.
     * @return Una cadena representando al usuario con su nombre y apellido.
     */
    @Override
    public String toString() {
        return nombre + " " + apellido;
    }

    // Implementación de métodos de la interfaz UserDetails de Spring Security

    /**
     * Obtiene los roles del usuario como autoridades para la gestión de acceso en Spring Security.
     * @return La colección de autoridades del usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    /**
     * Obtiene el nombre de usuario (correo electrónico).
     * @return El correo electrónico del usuario.
     */
    @Override
    public String getUsername() {
        return correo;
    }

    /**
     * Verifica si la cuenta del usuario ha expirado.
     * @return `true` ya que las cuentas de usuario no tienen expiración.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Verifica si la cuenta del usuario está bloqueada.
     * @return `true` ya que las cuentas de usuario no están bloqueadas.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Verifica si las credenciales del usuario han expirado.
     * @return `true` ya que las credenciales no tienen expiración.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Verifica si la cuenta del usuario está habilitada.
     * @return `true` ya que las cuentas de usuario están habilitadas por defecto.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Agrega un pedido a la lista de compras del usuario.
     * @param compra El pedido que se desea agregar.
     */
    public void agregarCompra(Pedido compra) {
        compras.add(compra);
    }
}
