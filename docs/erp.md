# docs/erp.md

## Especificación de Requisitos Software

### 1. Introducción
El proyecto consiste en un sistema de venta en línea de productos ecológicos. Permite la compra individual o en paquetes, gestión de inventario, cálculo de precios con descuentos e integración con múltiples métodos de pago.

### 2. Requisitos Funcionales

#### Venta de productos
- Compra individual o en paquetes.
- Cada producto tendrá nombre, descripción, precio, inventario y categoría.

#### Gestión de inventario (opcional)
- Agregar, editar y eliminar productos.
- Actualizar stock tras ventas o devoluciones.

#### Cálculo de precios
- Aplicación automática de descuentos sobre el precio.

#### Métodos de pago
- Pago por tarjeta, efectivo, transferencia, etc., con posible integración de pasarelas externas (opcional).

### 3. Requisitos No Funcionales

#### Seguridad
- Uso de HTTPS y encriptación para pagos.

#### Desempeño
- Páginas cargan en menos de 2 segundos y soportan múltiples usuarios simultáneamente.

#### Escalabilidad
- Facilita la adición de productos, categorías y métodos de pago.

#### Usabilidad
- Diseño intuitivo para administradores y clientes.

### 4. Tecnologías Utilizadas

- *Backend:* Spring Boot, Spring Security.
- *Frontend:* Thymeleaf, HTML5, CSS3, JS.
- *Base de datos:* PostgreSQL.