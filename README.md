# GA - 2024 POO2 - Integrador

# Sistema de Gestión de Productos Ecológicos

Bienvenido al sistema de gestión de productos ecológicos en línea. Este proyecto tiene como objetivo proporcionar una plataforma dedicada a la venta y compra de productos ecológicos. El mismo incluye  funcionalidades para clientes y administradores.

---

## Instalación

1. **Clona el repositorio**:
   ```bash
   git clone https://github.com/joaquinkuster/ecomisiones.git
   ```

2. **Crea una base de datos en PostgreSQL**:
   - Inicia sesión en tu servidor PostgreSQL utilizando tu herramienta preferida (pgAdmin, terminal, etc.).
   - Ejecuta el siguiente comando para crear la base de datos:
     ```sql
     CREATE DATABASE ecomisiones;
     ```
   - Asegúrate de tener las credenciales del usuario de PostgreSQL que vas a usar.

3. **Actualiza las credenciales en el archivo `application.properties`**:
   - Ve al archivo: `src/main/resources/application.properties`.
   - Configura los parámetros de conexión con tu base de datos PostgreSQL. Un ejemplo básico:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/ecomisiones
     spring.datasource.username=tu_usuario
     spring.datasource.password=tu_contraseña
     spring.jpa.hibernate.ddl-auto=update
     spring.jpa.show-sql=true
     ```
   - Reemplaza `tu_usuario` y `tu_contraseña` con tus credenciales de PostgreSQL.

4. **Instalación de Dependencias**
 - Abre una terminal y navega hasta el directorio raíz del proyecto.
 - Ejecuta el siguiente comando para instalar todas las dependencias necesarias:
   ```bash
   mvn clean install
   ```

5. **Ejecuta la aplicacion**
    ```bash
   mvn spring-boot:run
   ```

6. **Acceda al sistema**
   - Abre un navegador y ve a http://localhost:8080.
  
---

## Integrantes
* Joaquín Küster _(joaquinkuster)_ https://github.com/joaquinkuster
* Liam Bialy _(bialyLT)_ https://github.com/bialyLT
* Facal Daniela Lujan _(LujanDanielaFacal)_ https://github.com/LujanDanielaFacal
* Martinez Lazaro Ezequiel  _(lazamartinez)_ https://github.com/lazamartinez
