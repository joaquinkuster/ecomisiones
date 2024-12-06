package com.app.ecomisiones.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import java.time.LocalDate;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario("John", "Doe", "john.doe@example.com", "1234567890", "password");
    }

    @Test
    public void testUsuarioConstructor() {
        assertEquals("John", usuario.getNombre());
        assertEquals("Doe", usuario.getApellido());
        assertEquals("john.doe@example.com", usuario.getCorreo());
        assertEquals("1234567890", usuario.getTelefono());
        assertEquals("password", usuario.getPassword());
        assertNotNull(usuario.getFechaCreacion());
        assertEquals(LocalDate.now(), usuario.getFechaCreacion());
        assertEquals(RolUsuario.Usuario, usuario.getRol());
        assertFalse(usuario.getBaja());
    }

    @Test
    public void testEsInactivo() {
        assertFalse(usuario.esInactivo());
        usuario.setBaja(true);
        assertTrue(usuario.esInactivo());
    }

    @Test
    public void testToString() {
        assertEquals("John Doe", usuario.toString());
    }

    @Test
    public void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_Usuario", authorities.iterator().next().getAuthority());
    }

    @Test
    public void testGetUsername() {
        assertEquals("john.doe@example.com", usuario.getUsername());
    }

    @Test
    public void testIsAccountNonExpired() {
        assertTrue(usuario.isAccountNonExpired());
    }

    @Test
    public void testIsAccountNonLocked() {
        assertTrue(usuario.isAccountNonLocked());
    }

    @Test
    public void testIsCredentialsNonExpired() {
        assertTrue(usuario.isCredentialsNonExpired());
    }

    @Test
    public void testIsEnabled() {
        assertTrue(usuario.isEnabled());
    }

    @Test
    public void testAgregarCompra() {
        Pedido pedido = new Pedido();
        usuario.agregarCompra(pedido);
        assertTrue(usuario.getCompras().contains(pedido));
    }
}