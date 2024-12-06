# docs/dp-iteracion-1.md

## Trabajo en equipo
- *Joaquín Kuster:* Desarrollo del backend, incluyendo la lógica de negocio y los servicios para las funcionalidades.  
- *Lázaro Martínez:* Desarrollo del frontend, diseño de la interfaz y validaciones de formularios.  
- *Liam Bialy:* Desarrollo del frontend, diseño de la interfaz y validaciones de formularios.  

---

## Diseño OO
*Diagrama de clases UML:*  
Insertar diagrama

---

## Wireframe y Caso de Uso
- *Wireframe:*  
  Boceto simple de:
  - Página de detalles del producto.
  - Página de inicio de sesión y registro.
  - Página de visualización de pedidos del cliente.

- *Caso de Uso:*  
  - Un cliente inicia sesión y selecciona un producto para comprarlo de forma individual.  
  - El sistema calcula el precio con descuento y muestra los detalles del producto.  
  - El cliente completa el formulario de compra, elige una sucursal y un método de pago.  
  - El sistema calcula el envío y el tiempo estimado según los datos ingresados.

---

## Backlog de Iteración
Historias de usuario implementadas en la iteración 1:  
1. *HU1:* Compra individual de un producto.  
2. *HU2:* Calcular el precio de un producto con descuento.  
3. *HU3:* Inicio de sesión y registro de cuenta.  
4. *HU4:* Cálculo del envío y días de espera.  
5. *HU5:* Alta de nuevos productos como administrador.  
6. *HU6:* Modificar perfil del cliente.  
7. *HU7:* Ver pedidos como cliente.  
8. *HU8:* Incorporación de sucursales y medios de pago.  
9. *HU9:* Vista de detalles del producto.  

---

## Tareas
1. Backend:
   - Implementar lógica de compra individual.
   - Crear cálculo automático de descuentos.
   - Configurar servicios para el manejo de sucursales y métodos de pago.
   - Desarrollar endpoints para iniciar sesión, registrar cuentas y ver pedidos.

2. Frontend:
   - Diseñar página de detalles del producto con cálculo de descuentos.
   - Crear formularios de inicio de sesión, registro y modificación de perfil.
   - Implementar la visualización de pedidos del cliente.
   - Diseñar interfaz para el alta de nuevos productos.

3. Validaciones:
   - Validar formularios de registro y modificación de perfil.
   - Verificar que los datos de envío y métodos de pago sean correctos.
   - Actualizar stock tras realizar una compra.