package com.app.ecomisiones.utils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.app.ecomisiones.model.Producto;

public class Utilidades {

    public static List<String> convertir(Producto producto) {
        List<String> img = new ArrayList<>();
        img.addAll(producto.getImagenes()
                .stream()
                .map(imagen -> Base64.getEncoder().encodeToString(imagen.getImagen()))
                .toList());
        return img;
    }

    public static <T> List<T> limitar(List<T> lista, int max) {
        return lista.size() > max ? lista.subList(0, max) : lista;
    }    
}
