package com.app.ecomisiones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.app.ecomisiones.service.Usuario.UsuarioDetallesService;

/**
 * Configuración de seguridad para la aplicación.
 * Se configura la autenticación, autorización y las políticas de seguridad.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Inyección de dependencias para el servicio de detalles de usuario
    private final UsuarioDetallesService usuarioDetallesService;

    /**
     * Constructor para la configuración de seguridad.
     * @param usuarioDetallesService Servicio para obtener detalles del usuario.
     */
    public SecurityConfig(UsuarioDetallesService usuarioDetallesService) {
        this.usuarioDetallesService = usuarioDetallesService;
    }

    /**
     * Configura el proveedor de autenticación basado en un servicio de usuario personalizado.
     * @return DaoAuthenticationProvider configurado.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        
        // Configuramos el servicio de detalles de usuario y el codificador de contraseñas
        authProvider.setUserDetailsService(usuarioDetallesService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Configura el codificador de contraseñas usando BCrypt.
     * @return PasswordEncoder que usa BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt es un algoritmo seguro para almacenar contraseñas
    }

    /**
     * Configura el AuthenticationManager, necesario para manejar la autenticación.
     * @param authConfig Configuración de autenticación.
     * @return AuthenticationManager.
     * @throws Exception en caso de error en la configuración.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configura la cadena de filtros de seguridad, que maneja las reglas de acceso y autenticación.
     * @param http Configuración de seguridad HTTP.
     * @return SecurityFilterChain configurado.
     * @throws Exception en caso de error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF si no es necesario (útil en APIs o clientes de SPA)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/registro", "/public/**", "/css/**", "/js/**").permitAll() // Permite acceso sin autenticación a estas rutas
                        .requestMatchers("/admin", "/admin/**").hasRole("Administrador") // Solo usuarios con el rol "Administrador" pueden acceder
                        .anyRequest().authenticated() // Requiere autenticación para cualquier otra ruta
                )
                .formLogin(login -> login
                        .loginPage("/login") // Ruta personalizada para la página de login
                        .permitAll() // Permite acceso sin autenticación a la página de login
                        .defaultSuccessUrl("/inicio?login=true", true) // Redirige a la página principal tras login exitoso
                        .failureUrl("/login?error=true") // Redirige a login si ocurre un error
                )
                .userDetailsService(usuarioDetallesService) // Configura el servicio de detalles de usuario para la autenticación
                .logout(logout -> logout
                        .logoutUrl("/logout") // Ruta para realizar logout
                        .logoutSuccessUrl("/login?logout=true") // Redirige a la página de login tras logout exitoso
                        .permitAll() // Permite acceso sin autenticación a la ruta de logout
                )
                .build(); // Construye la configuración de seguridad
    }
}
