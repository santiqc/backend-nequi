package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.NombreNuevoDto;
import reactor.core.publisher.Mono;

public interface ProductoServicio {


    Mono<String> eliminarProducto(Long productoId);

    Mono<String> actualizarStockProducto(Long productoId, Integer nuevoStock);

    Mono<String> actualizarNombreProducto(Long productoId, NombreNuevoDto dto);
}
