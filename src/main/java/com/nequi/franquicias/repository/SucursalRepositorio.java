package com.nequi.franquicias.repository;

import com.nequi.franquicias.model.Sucursal;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface SucursalRepositorio extends R2dbcRepository<Sucursal, Long> {
    Flux<Sucursal> findByFranquiciaId(Long franquiciaId);
}
