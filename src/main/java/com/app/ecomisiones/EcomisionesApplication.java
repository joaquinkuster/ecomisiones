package com.app.ecomisiones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.app.ecomisiones.service.SemillaService;

/**
 * Clase principal de la aplicación que se ejecuta al iniciar el servicio Spring Boot.
 * Implementa {@link CommandLineRunner} para ejecutar tareas al arrancar la aplicación.
 */
@SpringBootApplication
public class EcomisionesApplication implements CommandLineRunner {

    /**
     * Servicio encargado de insertar los datos de semilla al inicio de la aplicación.
     */
    @Autowired
    private SemillaService semillaService;

    /**
     * Método principal de la aplicación que lanza el contexto de Spring Boot.
     * 
     * @param args Los argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(EcomisionesApplication.class, args);
    }

    /**
     * Método ejecutado al iniciar la aplicación. Se utiliza para insertar los datos de semilla.
     * Este método se ejecuta después de que Spring Boot haya arrancado el contexto.
     * 
     * @param args Los argumentos de la línea de comandos (no utilizados en este caso).
     * @throws Exception Si ocurre algún error durante la ejecución del método.
     */
    @Override
    public void run(String... args) throws Exception {
        // Ejecutamos la semilla cuando la aplicación arranca
        semillaService.insertarSemilla();
    }
}
