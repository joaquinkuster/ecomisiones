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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioDetallesService usuarioDetallesService;

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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable()) // Opcional: desactiva CSRF si no lo necesitas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/registro", "/public/**").permitAll() // Rutas públicas
                        .requestMatchers("/admin", "/admin/**").hasRole("Administrador")
                        .anyRequest().authenticated() // Todo lo demás requiere autenticación
                )
                .formLogin(login -> login
                        .loginPage("/login") // Página de login personalizada
                        .permitAll()
                        .defaultSuccessUrl("/inicio", true) // Redirige al inicio tras login exitoso
                        .failureUrl("/login?error=true") // Redirige en caso de error
                ).userDetailsService(usuarioDetallesService)
                .logout(logout -> logout
                        .logoutUrl("/logout") // Ruta para cerrar sesión
                        .logoutSuccessUrl("/login?logout=true") // Redirige tras logout exitoso
                        .permitAll())
                .build();
    }
}
