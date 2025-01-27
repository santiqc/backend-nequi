package com.nequi.franquicias.service;

import com.nequi.franquicias.model.Franquicia;
import com.nequi.franquicias.model.Producto;
import com.nequi.franquicias.model.Sucursal;
import reactor.core.publisher.Mono;

public interface FranquiciaServicio {

    Mono<Franquicia> crearFranquicia(Franquicia franquicia);

    Mono<Sucursal> agregarSucursal(Long franquiciaId, Long sucursalId);

    Mono<Producto> agregarProducto(Long sucursalId, Long productoIdLong);
}
