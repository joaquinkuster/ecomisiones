package com.app.ecomisiones.model;

/**
 * Enum que representa los diferentes medios de envío disponibles.
 * Cada tipo de envío tiene un costo y un número de días de espera asociados.
 */
public enum MedioDeEnvio {

    /**
     * Envío local con un costo de 500.0 y 1 día de espera.
     */
    Local(500.0, 1),

    /**
     * Envío provincial con un costo de 1500.0 y 3 días de espera.
     */
    Provincial(1500.0, 3),

    /**
     * Envío nacional con un costo de 3000.0 y 5 días de espera.
     */
    Nacional(3000.0, 5);

    /**
     * El costo del envío asociado a este medio de envío.
     */
    private final double costo;

    /**
     * El número de días de espera estimados para este medio de envío.
     */
    private final int diasDeEspera;

    /**
     * Constructor privado para inicializar los valores de costo y días de espera.
     * Este constructor es llamado internamente para cada uno de los valores de enum.
     *
     * @param costo El costo del envío.
     * @param diasDeEspera El número de días de espera para el envío.
     */
    MedioDeEnvio(double costo, int diasDeEspera) {
        this.costo = costo;
        this.diasDeEspera = diasDeEspera;
    }

    /**
     * Obtiene el costo asociado a este medio de envío.
     *
     * @return El costo del envío en unidades monetarias.
     */
    public double getCosto() {
        return costo;
    }

    /**
     * Obtiene el número de días de espera asociados a este medio de envío.
     *
     * @return El número de días de espera para el envío.
     */
    public int getDiasDeEspera() {
        return diasDeEspera;
    }
}
