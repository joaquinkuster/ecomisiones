package com.app.ecomisiones.model;

public enum MedioDeEnvio {
    Local(500.0, 1),   // Costo para el medio de envío local
    Provincial(1500.0, 3), // Costo para el medio de envío provincial
    Nacional(3000.0, 5); // Costo para el medio de envío nacional

    private final double costo;
    private final int diasDeEspera;

    // Constructor para inicializar el costo
    MedioDeEnvio(double costo, int diasDeEspera) {
        this.costo = costo;
        this.diasDeEspera = diasDeEspera;
    }

    // Método para obtener el costo
    public double getCosto() {
        return costo;
    }

    // Método para obtener el dias de espera
    public int getDiasDeEspera() {
        return diasDeEspera;
    }
}