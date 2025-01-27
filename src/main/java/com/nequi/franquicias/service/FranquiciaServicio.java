package com.nequi.franquicias.service;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FranquiciaServicio {

    Mono<Franquicia> crearFranquicia(Franquicia franquicia);

    Mono<Sucursal> agregarSucursal(Long franquiciaId, Long sucursalId);

    Mono<Producto> agregarProducto(Long sucursalId, Long productoIdLong);

    Mono<String> eliminarProducto(Long productoId);

    Mono<String> actualizarStockProducto(Long productoId, Integer nuevoStock);

    Flux<Producto> obtenerProductosConMayorStock(Long franquiciaId);
}
