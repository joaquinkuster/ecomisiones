# docs/dp-iteracion-2.md

## Trabajo en equipo
- *Joaquín Kuster:* Desarrollo del backend, incluyendo la lógica de negocio para el carrito de compras y la gestión de pedidos.
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
  - Carrito de compras con productos seleccionados.
  - Página de búsqueda de productos.
  - Vista de pedidos y productos destacados.
  - Páginas de administración de pedidos.

- *Caso de Uso:*  
  - Un cliente selecciona varios productos para comprar en un paquete y agrega los productos al carrito.  
  - El sistema permite ver el carrito, aplicar descuentos y calcular el precio total.  
  - El administrador puede ver todos los pedidos realizados hasta la fecha.  
  - Un cliente puede buscar productos destacados basados en la cantidad de ventas.  
  - Los formularios de la aplicación (registro, compra, etc.) se validan correctamente antes de enviarlos.

---

## Backlog de Iteración
Historias de usuario implementadas en la iteración 2:  
1. *HU1:* Compra por paquetes de varios productos (Carrito de compras).  
2. *HU3:* Buscador de productos.  
3. *HU4:* Ver pedidos como administrador.  
4. *HU5:* Búsqueda de productos destacados.  
5. *HU6:* Validaciones en formularios clave.  
6. *HU7:* Diseñar varias vistas y elementos clave de la interfaz.  

---

## Tareas
1. Backend:
   - Implementar funcionalidad para agregar productos al carrito de compras.
   - Desarrollar la lógica de cálculo de precios en el carrito, aplicando descuentos.
   - Implementar el sistema de búsqueda de productos.
   - Crear endpoints para que el administrador vea todos los pedidos.
   - Implementar validación en los formularios clave.

2. Frontend:
   - Diseñar y mostrar el carrito de compras con opción de agregar o eliminar productos.
   - Crear la interfaz para la búsqueda de productos y visualización de productos destacados.
   - Desarrollar la vista para que los administradores puedan ver los pedidos.
   - Crear validaciones visuales en los formularios clave para registro y compra.

3. Validaciones:
   - Asegurar que todos los formularios clave sean validados correctamente antes de ser enviados.
   - Verificar el correcto funcionamiento del carrito de compras con la cantidad de productos y descuentos aplicados.
   - Validar que los productos añadidos al carrito sean actualizados correctamente al modificar cantidades.