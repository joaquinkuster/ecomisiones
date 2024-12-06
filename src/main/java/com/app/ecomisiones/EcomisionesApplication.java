package com.app.ecomisiones;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.ecomisiones.model.Almacen;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Ciudad;
import com.app.ecomisiones.model.Provincia;
import com.app.ecomisiones.model.RolUsuario;
import com.app.ecomisiones.model.Sucursal;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.AlmacenRepository;
import com.app.ecomisiones.repository.CategoriaRepository;
import com.app.ecomisiones.repository.CiudadRepository;
import com.app.ecomisiones.repository.ProvinciaRepository;
import com.app.ecomisiones.repository.SucursalRepository;
import com.app.ecomisiones.repository.UsuarioRepository;

@SpringBootApplication
public class EcomisionesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcomisionesApplication.class, args);
    }

    @Bean
    public CommandLineRunner initAdmin(UsuarioRepository usuarioRepository, 
                                       CategoriaRepository categoriaRepository,
                                       ProvinciaRepository provinciaRepository,
                                       CiudadRepository ciudadRepository,
                                       SucursalRepository sucursalRepository,
                                       AlmacenRepository almacenRepository, // Añadir repositorio de Almacen
                                       PasswordEncoder passwordEncoder) {
        return args -> {

            // Crear el administrador por defecto
            String adminEmail = "admin@correo.com";
            if (usuarioRepository.findByCorreo(adminEmail).isEmpty()) {
                Usuario admin = new Usuario(
                        "Admin",
                        "Sistema",
                        adminEmail,
                        "123456789",
                        passwordEncoder.encode("1234567")
                );
                admin.setRol(RolUsuario.Administrador);
                usuarioRepository.save(admin);
                System.out.println("Administrador predeterminado creado con éxito.");
            } else {
                System.out.println("El administrador ya existe.");
            }

            // Insertar provincias
            Provincia provincia1 = new Provincia("Misiones");
            provinciaRepository.save(provincia1);

            // Insertar ciudades
            Ciudad ciudad1 = new Ciudad("Posadas", provincia1);
            ciudadRepository.save(ciudad1);
            Ciudad ciudad2 = new Ciudad("Apóstoles", provincia1);
            ciudadRepository.save(ciudad2);

            // Insertar sucursales
            Sucursal sucursal1 = new Sucursal("Calle Ficticia 123", ciudad1);
            sucursalRepository.save(sucursal1);
            Sucursal sucursal2 = new Sucursal("Avenida Libertad 456", ciudad2);
            sucursalRepository.save(sucursal2);

            // Insertar categorías
            Categoria categoria1 = new Categoria("Electrónica", "Productos electrónicos de todo tipo.");
            Categoria categoria2 = new Categoria("Alimentos", "Alimentos frescos y envasados.");
            categoriaRepository.save(categoria1);
            categoriaRepository.save(categoria2);

            // Agregar subcategoría
            Categoria subCategoria1 = new Categoria("Móviles", "Teléfonos móviles y accesorios");
            subCategoria1.setPadre(categoria1);
            categoriaRepository.save(subCategoria1);

            // Insertar almacenes
            Almacen almacen1 = new Almacen("Av. Mitre 123", ciudad1);
            Almacen almacen2 = new Almacen("Calle San Martín 789", ciudad2);
            almacenRepository.save(almacen1);
            almacenRepository.save(almacen2);

            System.out.println("Datos iniciales insertados con éxito.");
        };
    }
}
