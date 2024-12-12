package com.app.ecomisiones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.ecomisiones.model.Almacen;
import com.app.ecomisiones.model.Carrito;
import com.app.ecomisiones.model.Categoria;
import com.app.ecomisiones.model.Ciudad;
import com.app.ecomisiones.model.MedioDePago;
import com.app.ecomisiones.model.Provincia;
import com.app.ecomisiones.model.RolUsuario;
import com.app.ecomisiones.model.Sucursal;
import com.app.ecomisiones.model.Usuario;
import com.app.ecomisiones.repository.AlmacenRepository;
import com.app.ecomisiones.repository.CarritoRepository;
import com.app.ecomisiones.repository.CategoriaRepository;
import com.app.ecomisiones.repository.CiudadRepository;
import com.app.ecomisiones.repository.MedioDePagoRepository;
import com.app.ecomisiones.repository.ProvinciaRepository;
import com.app.ecomisiones.repository.SucursalRepository;
import com.app.ecomisiones.repository.UsuarioRepository;

/**
 * Servicio para insertar datos iniciales en la base de datos (semilla).
 * Este servicio se utiliza para crear un usuario administrador y otros datos
 * como provincias, ciudades, sucursales, categorías de productos, almacenes
 * y medios de pago predeterminados.
 */
@Service
public class SemillaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private CiudadRepository ciudadRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private AlmacenRepository almacenRepository;

    @Autowired
    private MedioDePagoRepository medioDePagoRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Método que se ejecuta automáticamente para insertar datos iniciales
     * en la base de datos cuando el contexto de la aplicación se haya inicializado.
     * 
     * Este método crea un usuario administrador predeterminado, junto con su carrito,
     * provincias, ciudades, sucursales, categorías de productos, subcategorías, almacenes
     * y medios de pago.
     */
    @Transactional
    public void insertarSemilla() {
        // Verificar si ya existe el usuario administrador por correo
        String adminEmail = "admin@correo.com";
        if (!usuarioRepository.findByCorreoAndBajaFalse(adminEmail).isPresent()) {
            // Crear usuario administrador si no existe
            Usuario admin = new Usuario(
                    "Admin", 
                    "Admin", 
                    adminEmail, 
                    "123456789", 
                    passwordEncoder.encode("123456"));
            admin.setRol(RolUsuario.Administrador); // Establecer rol de administrador
            usuarioRepository.save(admin);

            // Crear carrito para el administrador
            Carrito carrito = new Carrito(admin);
            carritoRepository.save(carrito);

            // Insertar provincias
            Provincia provincia1 = new Provincia("Misiones");
            provinciaRepository.save(provincia1);

            // Insertar ciudades para la provincia de Misiones
            Ciudad ciudad1 = new Ciudad("Posadas", provincia1);
            ciudadRepository.save(ciudad1);
            Ciudad ciudad2 = new Ciudad("Apóstoles", provincia1);
            ciudadRepository.save(ciudad2);
            Ciudad ciudad3 = new Ciudad("Puerto Iguazú", provincia1);
            ciudadRepository.save(ciudad3);

            // Insertar sucursales en cada ciudad
            Sucursal sucursal1 = new Sucursal("Avenida Libertad 456", ciudad1);
            sucursalRepository.save(sucursal1);
            Sucursal sucursal2 = new Sucursal("Urquiza 274", ciudad2);
            sucursalRepository.save(sucursal2);
            Sucursal sucursal3 = new Sucursal("Santa Rosa 60", ciudad3);
            sucursalRepository.save(sucursal3);

            // Insertar categorías de productos
            Categoria categoria1 = new Categoria("Accesorios", "--");
            Categoria categoria2 = new Categoria("Higiene personal", "--");
            Categoria categoria3 = new Categoria("Maquillaje", "--");
            Categoria categoria4 = new Categoria("Cuidado del cabello", "--");
            categoriaRepository.save(categoria1);
            categoriaRepository.save(categoria2);
            categoriaRepository.save(categoria3);
            categoriaRepository.save(categoria4);

            // Insertar subcategorías para cada categoría
            Categoria subCategoria1 = new Categoria("Bolsos ecológicos", "--");
            subCategoria1.setPadre(categoria1);
            categoriaRepository.save(subCategoria1);

            Categoria subCategoria2 = new Categoria("Zapatos ecológicos", "--");
            subCategoria2.setPadre(categoria1);
            categoriaRepository.save(subCategoria2);

            Categoria subCategoria3 = new Categoria("Jabones naturales", "--");
            subCategoria3.setPadre(categoria2);
            categoriaRepository.save(subCategoria3);

            Categoria subCategoria4 = new Categoria("Pastas de dientes ecológicas", "--");
            subCategoria4.setPadre(categoria2);
            categoriaRepository.save(subCategoria4);

            Categoria subCategoria5 = new Categoria("Sombras y delineadores", "--");
            subCategoria5.setPadre(categoria3);
            categoriaRepository.save(subCategoria5);

            Categoria subCategoria6 = new Categoria("Labiales ecológicos", "--");
            subCategoria6.setPadre(categoria3);
            categoriaRepository.save(subCategoria6);

            Categoria subCategoria7 = new Categoria("Shampoos orgánicos", "--");
            subCategoria7.setPadre(categoria4);
            categoriaRepository.save(subCategoria7);

            Categoria subCategoria8 = new Categoria("Aceites para el cabello", "--");
            subCategoria8.setPadre(categoria4);
            categoriaRepository.save(subCategoria8);

            Categoria subCategoria9 = new Categoria("Cepillos ecológicos", "--");
            subCategoria9.setPadre(categoria1);
            categoriaRepository.save(subCategoria9);

            // Insertar almacenes
            Almacen almacen1 = new Almacen("Avenida Mitre 123", ciudad1);
            Almacen almacen2 = new Almacen("San Martín 789", ciudad2);
            almacenRepository.save(almacen1);
            almacenRepository.save(almacen2);

            // Insertar medios de pago
            MedioDePago medioDePago = new MedioDePago("Efectivo");
            medioDePagoRepository.save(medioDePago);

            medioDePago = new MedioDePago("Transferencia");
            medioDePagoRepository.save(medioDePago);

            medioDePago = new MedioDePago("Tarjeta de crédito");
            medioDePagoRepository.save(medioDePago);

            System.out.println("Aviso! Se han insertado los datos iniciales correctamente.");
        } else {
            System.out.println("Aviso! El sistema ya posee La semilla por defecto.");
        }
    }
}
