package com.app.ecomisiones.helper;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.app.ecomisiones.model.Producto;

public class Img {

    public static List<String> convertir(Producto producto) {
        List<String> img = new ArrayList<>();
        img.addAll(producto.getImagenes()
                .stream()
                .map(imagen -> Base64.getEncoder().encodeToString(imagen.getImagen()))
                .toList());
        return img;
    }
}
