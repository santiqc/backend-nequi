package com.nequi.franquicias.service;

import reactor.core.publisher.Mono;

public interface ProductoServicio {


    Mono<String> eliminarProducto(Long productoId);

    Mono<String> actualizarStockProducto(Long productoId, Integer nuevoStock);


}
