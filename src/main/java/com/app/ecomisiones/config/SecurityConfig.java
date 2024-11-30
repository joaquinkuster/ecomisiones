package com.app.ecomisiones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.Authentication;

import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.service.Usuario.UsuarioDetallesService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioDetallesService usuarioDetallesService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    public SecurityConfig(UsuarioDetallesService usuarioDetallesService) {
        this.usuarioDetallesService = usuarioDetallesService;

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(usuarioDetallesService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF si es necesario
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/registro", "/public/**").permitAll() // Rutas públicas
                        .requestMatchers("/admin", "/admin/**").hasRole("Administrador") // Rutas públicas
                        .anyRequest().authenticated() // Protege todas las demás rutas
                )

                .formLogin(form -> form
                        .loginPage("/login") // Página personalizada para el login
                        .defaultSuccessUrl("/inicio", true) // Redirección después del login exitoso
                        .permitAll())
                .userDetailsService(usuarioDetallesService)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        return http.build();
    }

    // Este método obtiene el usuario autenticado
    public Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Suponiendo que tienes un objeto Usuario como principal
            return (Usuario) authentication.getPrincipal();
        }
        return null;
    }
}
