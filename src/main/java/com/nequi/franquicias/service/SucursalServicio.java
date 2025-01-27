package com.nequi.franquicias.service;

import com.nequi.franquicias.dto.ProductoDTO;
import com.nequi.franquicias.model.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalServicio {

    Mono<Producto> agregarProducto(Long sucursalId, ProductoDTO productoDTO);

    Flux<Producto> obtenerProductosConMayorStock(Long franquiciaId);
}
