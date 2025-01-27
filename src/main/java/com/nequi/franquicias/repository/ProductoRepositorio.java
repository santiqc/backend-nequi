package com.nequi.franquicias.repository;

import com.nequi.franquicias.model.Producto;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ProductoRepositorio extends R2dbcRepository<Producto, Long> {
    Flux<Producto> findBySucursalId(Long sucursalId);

    Flux<Producto> findBySucursalIdOrderByStockDesc(Long sucursalId);
}