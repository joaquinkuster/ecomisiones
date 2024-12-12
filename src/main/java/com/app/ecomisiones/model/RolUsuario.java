package com.app.ecomisiones.model;

/**
 * Enum que representa los roles de usuario en el sistema.
 * Los roles disponibles son:
 * - Administrador: Tiene acceso completo al sistema.
 * - Usuario: Tiene acceso limitado al sistema.
 */
public enum RolUsuario {
    /**
     * Rol de administrador con acceso completo a todas las funcionalidades del sistema.
     */
    Administrador,

    /**
     * Rol de usuario con acceso limitado a funcionalidades específicas del sistema.
     */
    Usuario
}
