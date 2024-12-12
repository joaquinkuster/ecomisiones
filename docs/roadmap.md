# docs/roadmap.md

# Roadmap del Proyecto

Este es el plan tentativo para las historias de usuario que se entregarán en cada iteración del proyecto. El objetivo es seguir un enfoque iterativo, trabajando en funcionalidades clave durante cada ciclo de dos semanas. A medida que el proyecto avanza, se integran nuevas características y se refina la interfaz para mejorar la experiencia del usuario y la gestión del sistema.

---

## Iteración 1 (Semana 1)
Durante la primera iteración, nos enfocaremos en establecer las funcionalidades fundamentales para la compra y el registro de productos, así como las primeras funcionalidades de administración y gestión de usuarios.

### Historias de usuario implementadas en la iteración 1:

#### **HU1: Compra individual de un producto**
**Descripción de la historia de usuario**  
Como cliente,  
Quiero poder comprar un producto individual  
Para realizar una compra rápida y sencilla.
**Criterios de aceptación**  
Listado:
- El cliente debe seleccionar un producto de la vista.
- El sistema debe mostrar el precio actualizado con descuentos (si aplica).
- Al confirmar la compra, se debe restar la cantidad comprada del stock.

#### **HU2: Calcular el precio de un producto con descuento**
**Descripción de la historia de usuario**  
Como cliente,  
Quiero que el sistema calcule automáticamente el precio del producto con descuento  
Para saber cuánto debo pagar.
**Criterios de aceptación**  
Listado:
- El descuento debe aplicarse automáticamente al precio del producto.
- El precio final debe mostrarse en la vista del producto y en el carrito.

#### **HU3: Inicio de sesión y registro de cuenta**
**Descripción de la historia de usuario**  
Como usuario,  
Quiero iniciar sesión o registrar una nueva cuenta  
Para acceder al sistema y personalizar mi experiencia.
**Criterios de aceptación**  
Listado:
- El sistema debe validar campos en el formulario de registro.
- El inicio de sesión debe requerir correo y contraseña válidos.
- La cuenta debe almacenarse en el sistema con datos básicos del usuario.

#### **HU4: Cálculo del envío y días de espera**
**Descripción de la historia de usuario**  
Como cliente,  
Quiero saber el costo y tiempo de envío según el destino y tipo de envío  
Para saber cuándo y cuánto me costará recibir mis productos.
**Criterios de aceptación**  
Listado:
- El sistema debe calcular el envío según si es local, provincial o nacional.
- Si más de un producto sale de la misma ciudad, el costo de envío debe unificarse.
- El cálculo debe incluir días estimados de espera.

#### **HU5: Alta de nuevos productos como administrador**
**Descripción de la historia de usuario**  
Como administrador,  
Quiero dar de alta nuevos productos en el sistema  
Para mantener actualizado el catálogo de la tienda.
**Criterios de aceptación**  
Listado:
- El formulario debe validar campos obligatorios como nombre, precio y stock.
- El producto debe asignarse a una categoría y almacén específico.
- Al registrarse, el producto debe ser visible en el catálogo.

#### **HU6: Modificar perfil del cliente**
**Descripción de la historia de usuario**  
Como cliente,  
Quiero modificar mis datos personales y mi contraseña  
Para mantener mi perfil actualizado y seguro.
**Criterios de aceptación**  
Listado:
- El cliente debe poder actualizar nombre, apellido, correo y teléfono.
- El cliente debe poder cambiar su contraseña con validación de la actual.

#### **HU7: Ver pedidos como cliente**
**Descripción de la historia de usuario**  
Como cliente,  
Quiero ver los pedidos que he hecho y sus detalles  
Para tener un registro de mis compras.
**Criterios de aceptación**  
Listado:
- El cliente debe poder ver una lista de pedidos realizados.
- Cada pedido debe mostrar detalles como productos, precios, envío y coste.

#### **HU8: Incorporación de sucursales y medios de pago**
**Descripción de la historia de usuario**  
Como cliente,  
Quiero poder seleccionar una sucursal y un medio de pago durante mi compra  
Para que mi experiencia sea completa y flexible.
**Criterios de aceptación**  
Listado:
- El cliente debe elegir la sucursal donde recoger el pedido o a la que será enviado.
- El cliente debe seleccionar un medio de pago compatible (efectivo, tarjeta, etc.).

#### **HU9: Vista de detalles del producto**
**Descripción de la historia de usuario**  
Como cliente,  
Quiero ver una página detallada del producto  
Para conocer todas sus características antes de comprar.
**Criterios de aceptación**  
Listado:
- La vista debe incluir el nombre, precio, descuento, descripción, y stock disponible.
- La página debe mostrar imágenes del producto y opciones para añadir al carrito.
- El sistema debe ser capaz de calcular y mostrar en pantalla el precio con descuento.
- El cliente puede comprar por cantidad dependiendo del stock disponible.
- El cliente puede agregar al carrito dependiendo del stock disponible.
- El cliente puede acceder a los productos relacionados con el producto que quiere comprar.

#### **HU10: Vista de la página principal**
**Descripción de la historia de usuario**  
Como usuario,  
Quiero poder visualizar todos los productos disponibles  
Para poder acceder a los detalles de cada producto que se muestre en el sistema.
**Criterios de aceptación**  
Listado:
- El sistema debe ser capaz de mostrar todos los productos que se han cargado.

#### **HU11: Vista del formulario para agregar un nuevo producto**
**Descripción de la historia de usuario**  
Como administrador,  
Quiero publicar un nuevo producto en el sistema, agregando los siguientes datos:  
Nombre del producto, Descripción, Precio, Descuento (%), Peso (en Kg), Stock, Categoría, Almacén y subir una imagen asociada al producto.
**Criterios de aceptación**  
Listado:
- El sistema debe validar los datos ingresados por el administrador.
- El sistema debe ser capaz de almacenar fotos en la base de datos.

#### **HU12: Vista del formulario para comprar un pedido**
**Descripción de la historia de usuario**  
Como usuario,  
Quiero tener una sección donde pueda realizar una transacción para el producto que quiero comprar.  
Quiero poder actualizar mis datos de forma sencilla si es necesario.  
Quiero poder seleccionar una sucursal.  
Quiero poder seleccionar un método de pago.
**Criterios de aceptación**  
Listado:
- El sistema debe ser capaz de mostrar en el formulario los productos a ordenar con nombre, precio y cantidad.
- El sistema debe ser capaz de calcular la suma total en pesos.
- El sistema debe ser capaz de calcular el costo de envío.
- El sistema debe ser capaz de calcular los días que tardará en llegar dependiendo de la distancia del cliente a la sucursal.

---
## Iteración 2 (Semana 2)
En la segunda iteración, se implementarán mejoras en la funcionalidad de compra, búsqueda de productos y administración de pedidos. También se trabajará en la validación de formularios y la creación de vistas clave para la interfaz de usuario.

### Historias de usuario implementadas en la iteración 2:

#### **HU1: Compra por paquetes de varios productos (Carrito de compras)**  
**Descripción de la historia de usuario**  
Como cliente,  
Quiero agregar varios productos al carrito y comprar en una sola transacción  
Para optimizar mi tiempo de compra.
**Criterios de aceptación**  
Listado:
- Los productos deben poder añadirse o eliminarse del carrito.
- El sistema debe verificar si hay suficiente stock al añadir un producto.
- Al completar la compra, el stock de cada producto debe actualizarse.

#### **HU2: Buscador de productos**  
**Descripción de la historia de usuario**  
Como cliente,  
Quiero buscar productos aplicando filtros por categoría, precio y descuento  
Para encontrar fácilmente lo que necesito.
**Criterios de aceptación**  
Listado:
- El buscador debe permitir filtrar por categorías disponibles.
- El buscador debe permitir filtrar hasta un precio específico.
- El buscador debe permitir filtrar desde un cierto descuento.

#### **HU3: Ver pedidos como administrador**  
**Descripción de la historia de usuario**  
Como administrador,  
Quiero ver todos los pedidos realizados hasta ahora  
Para gestionar el flujo de ventas de la tienda.
**Criterios de aceptación**  
Listado:
- El administrador debe acceder a una vista de todos los pedidos.
- Cada pedido debe mostrar detalles como productos, cliente, envío y coste.

#### **HU4: Búsqueda de productos destacados**  
**Descripción de la historia de usuario**  
Como cliente,  
Quiero buscar productos destacados en base a sus ventas  
Para conocer los productos más populares.
**Criterios de aceptación**  
Listado:
- Los productos destacados deben mostrarse según el número de ventas acumuladas.
- La vista debe ser fácil de navegar y mostrar detalles básicos de cada producto.

#### **HU5: Validaciones en formularios clave**  
**Descripción de la historia de usuario**  
Como desarrollador,  
Quiero validar los formularios de registro, alta de productos y carrito  
Para evitar errores o inconsistencias en el sistema.
**Criterios de aceptación**  
Listado:
- El formulario de registro debe validar campos como nombre, correo y contraseña.
- El formulario de alta de productos debe validar nombre, precio, categoría y stock.
- El carrito debe verificar que los productos seleccionados tienen suficiente stock.

#### **HU6: Diseñar varias vistas y elementos clave de la interfaz**  
**Descripción de la historia de usuario**  
Como diseñador,  
Quiero desarrollar componentes visuales como la barra de navegación, el inicio, el carrito y el perfil  
Para ofrecer una experiencia de usuario intuitiva y atractiva.
**Criterios de aceptación**  
Listado:
- La barra de navegación debe incluir acceso rápido a categorías, carrito y perfil.
- El inicio debe tener un hero, filtros, y secciones para productos destacados y últimos lanzamientos.
- El perfil debe permitir la edición de datos personales y contraseña.
- El carrito debe mostrar productos, cantidades y el precio total actualizado.

---